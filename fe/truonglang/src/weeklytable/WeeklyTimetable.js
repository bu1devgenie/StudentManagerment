import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import Navbar from '../Navbar';
import './WeeklyTimetable.css';
import Cookies from 'js-cookie';
import ReactPaginate from 'react-paginate';
import {Modal, Button} from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import AsyncSelect from 'react-select/async';
function WeeklyTimetable() {
    const navigate = useNavigate();
    const accessToken = Cookies.get('accessToken');
    const email = Cookies.get('email');
    const name = Cookies.get('name');
    const ms = Cookies.get('ms');
    const roles = Cookies.get('roles');


    // State to hold the selected year and week
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
    const getCurrentWeek = () => {
        const currentDate = new Date();
        const firstDayOfYear = new Date(currentDate.getFullYear(), 0, 1);
        const daysOffset = 1 - firstDayOfYear.getDay();
        const firstMondayOfYear = new Date(firstDayOfYear);
        firstMondayOfYear.setDate(firstDayOfYear.getDate() + daysOffset + 7);

        const diff = currentDate - firstMondayOfYear;
        const currentWeekNumber = 1 + Math.floor(diff / 604800000); // 604800000 = 7 * 24 * 60 * 60 * 1000 (milliseconds in a week)

        const weekStartDate = new Date(currentDate);
        weekStartDate.setDate(currentDate.getDate() - currentDate.getDay() + 1); // Set to the first day of the current week (assuming Monday is the first day)
        const weekEndDate = new Date(currentDate);
        weekEndDate.setDate(currentDate.getDate() - currentDate.getDay() + 7); // Set to the last day of the current week (assuming Sunday is the last day)

        // Format dates as strings in the desired format (e.g., "9/1/2023 - 15/1/2023")
        const formattedStartDate = `${weekStartDate.getDate()}/${weekStartDate.getMonth() + 1}/${weekStartDate.getFullYear()}`;
        const formattedEndDate = `${weekEndDate.getDate()}/${weekEndDate.getMonth() + 1}/${weekEndDate.getFullYear()}`;
        const currentWeekRange = `${formattedStartDate} - ${formattedEndDate}`;

        return currentWeekRange;
    };
    const [selectedWeek, setSelectedWeek] = useState(getCurrentWeek());
    const [dataWeek, setDataWeek] = useState([]);
    // Function to generate the options for the year select
    const generateYearOptions = () => {
        // Get the current year
        const currentYear = new Date().getFullYear();

        // Define the range of years
        const range = 2;

        // Generate the year options dynamically
        const years = Array.from({length: range * 2 + 1}, (_, index) => currentYear - range + index);

        return years.map((year) => (
            <option key={year} value={year} selected={year === selectedYear}>
                {year}
            </option>
        ));
    };
    // Function to generate the options for the week select
    const generateWeekOptions = (selectedYear) => {
        const weeks = [];

        // Get the first day of the year
        const firstDayOfYear = new Date(selectedYear, 0, 1);

        // Get the first Monday of the year
        const firstMondayOfYear = new Date(firstDayOfYear);
        firstMondayOfYear.setDate(firstDayOfYear.getDate() + ((1 - firstDayOfYear.getDay() + 7) % 7));

        // Get the last day of the year
        const lastDayOfYear = new Date(selectedYear, 11, 31);

        // Loop through each week and add it to the options
        let currentMonday = new Date(firstMondayOfYear);
        while (currentMonday <= lastDayOfYear) {
            const weekNumber = getWeekNumber(currentMonday);
            const weekStartDate = new Date(currentMonday);
            const weekEndDate = new Date(currentMonday);
            weekEndDate.setDate(currentMonday.getDate() + 6); // Assuming weeks end on Sunday

            // Format dates as strings (you can customize the format as needed)
            const formattedStartDate = `${weekStartDate.getDate()}/${weekStartDate.getMonth() + 1}/${weekStartDate.getFullYear()}`;
            const formattedEndDate = `${weekEndDate.getDate()}/${weekEndDate.getMonth() + 1}/${weekEndDate.getFullYear()}`;

            weeks.push({
                weekNumber,
                startDate: formattedStartDate,
                endDate: formattedEndDate,
            });

            currentMonday.setDate(currentMonday.getDate() + 7);
        }

        return weeks.map((week) => (
            <option key={`${week.startDate} - ${week.endDate}`} value={`${week.startDate} - ${week.endDate}`}>
                {`${week.startDate} - ${week.endDate}`}
            </option>
        ));
    };
    // Function to get the week number from a given date
    const getWeekNumber = (date) => {
        const firstDayOfYear = new Date(date.getFullYear(), 0, 1);
        const daysOffset = 1 - firstDayOfYear.getDay();
        const firstMondayOfYear = new Date(firstDayOfYear);
        firstMondayOfYear.setDate(firstDayOfYear.getDate() + daysOffset + 7);
        const diff = date - firstMondayOfYear;
        return 1 + Math.floor(diff / 604800000); // 604800000 = 7 * 24 * 60 * 60 * 1000 (milliseconds in a week)
    };


    // {
    //     "roomName": "Room-00000",
    //     "className": "Architecture-1-Summer-2023",
    //     "sesionDate": "2023-06-01",
    //     "slotOfDay": 1,
    //     "teacherName": "Bùi Văn Phúc",
    //     "attendence": null
    // },
    // {
    //     "roomName": "Room-00000",
    //     "className": "Architecture-1-Summer-2023",
    //     "sesionDate": "2023-06-01",
    //     "slotOfDay": 2,
    //     "teacherName": "Bùi Văn Phúc",
    //     "attendence": null
    // }


    const parseDate = (dateString) => {
        const [day, month, year] = dateString.split('/');
        return new Date(year, month - 1, day);
    };
    const generateTableContent = () => {
        const tableContent = [];
        const startWeekDate = parseDate(selectedWeek.split(' - ')[0]);
        const endWeekDate = parseDate(selectedWeek.split(' - ')[1]);
        console.log(dataWeek);

        // Replace 11 with the number of slots you want to display
        for (let slot = 1; slot < 11; slot++) {
            const rowData = (
                <tr key={slot}>
                    <td>Slot {slot}</td>
                    {/* Generate an array of dates for the selected week */}
                    {Array.from({length: 7}, (_, dayIndex) => {
                        const currentDate = new Date(startWeekDate);
                        currentDate.setDate(startWeekDate.getDate() + dayIndex);
                        const formattedDate = `${currentDate.getFullYear()}-${(currentDate.getMonth() + 1).toString().padStart(2, '0')}-${currentDate.getDate().toString().padStart(2, '0')}`;
                        // Find data for the corresponding slot and formattedDate
                        const matchingData = dataWeek.find(item => item.slotOfDay === slot && item.sesionDate === formattedDate);

                        return (
                            <td key={dayIndex}>
                                {matchingData ? (
                                    <div className="table-cell">
                                        <p className="class-name">{matchingData.className}</p>
                                        <p className="room-name">at {matchingData.roomName}</p>
                                        <p className="teacher-name">teacher: {matchingData.teacherName}</p>
                                        {matchingData.attendence !== null ? (
                                            <p className={`attendence ${matchingData.attendence}`}>
                                                {matchingData.attendence}
                                            </p>
                                        ) : (
                                            <p className="absent">(null)</p>
                                        )}
                                    </div>
                                ) : (
                                    "-"
                                )}
                            </td>
                        );
                    })}
                </tr>
            );

            tableContent.push(rowData);
        }

        return tableContent;
    };
    useEffect(() => {
        fetchWeeklytable();
    }, [selectedWeek]);

    const formatDate = (dateString) => {
        const [day, month, year] = dateString.split('/');
        const formattedMonth = month.padStart(2, '0');
        const formattedDay = day.padStart(2, '0');
        return `${year}-${formattedMonth}-${formattedDay}`;
    };
    const fetchWeeklytable = async (page) => {
        try {
            let formDataWeek = new FormData();
            var startWeek = selectedWeek.split('-')[0].trim();
            var endWeek = selectedWeek.split('-')[1].trim();
            const startWeekFormatted = formatDate(startWeek);
            const endWeekFormatted = formatDate(endWeek);
            formDataWeek.append('startWeek', startWeekFormatted);
            formDataWeek.append('endWeek', endWeekFormatted);
            formDataWeek.append('roles', roles);
            formDataWeek.append('ms', ms);


            var url = `http://localhost:9999/schedule/weeklyTimetable`;
            const response = await fetch(
                url,
                {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + accessToken
                    },
                    body: formDataWeek
                }
            );
            if (!response.ok) {
                throw new Error('Error fetching data');
            }
            const data = await response.json();
            setDataWeek(data);
        } catch (error) {
            alert(error.message);
        }
    };
    const generateTableHeader = (selectedWeek) => {
        const [startDateStr, endDateStr] = selectedWeek.split(' - ');
        const startDate = parseDate(startDateStr);
        const endDate = parseDate(endDateStr);

        const tableHeader = [];
        const currentDate = new Date(startDate);

        while (currentDate <= endDate) {
            const formattedDate = `${currentDate.getDate().toString().padStart(2, '0')}/${(currentDate.getMonth() + 1).toString().padStart(2, '0')}`;
            tableHeader.push(<th key={formattedDate} align="center">{formattedDate}</th>);
            currentDate.setDate(currentDate.getDate() + 1);
        }

        return <tr>{tableHeader}</tr>;
    };





    // Render the table with the data from the HTML
    return (
        <div>
            <Navbar />
            <div>
                <h2>Activities for {email} ({name})</h2>
                <p>
                    <b>Note</b>: These activities do not include extra-curriculum activities, such as
                    club activities ...
                </p>
                <p>
                    <b>Chú thích</b>: Các hoạt động trong bảng dưới không bao gồm hoạt động ngoại khóa,
                    ví dụ như hoạt động câu lạc bộ ...
                </p>
                <p>
                    Các phòng bắt đầu bằng AL thuộc tòa nhà Alpha. VD: AL...
                    <br />
                    Các phòng bắt đầu bằng BE thuộc tòa nhà Beta. VD: BE,..
                    <br />
                    Các phòng bắt đầu bằng G thuộc tòa nhà Gamma. VD: G201,...
                    <br />
                    Các phòng tập bằng đầu bằng R thuộc khu vực sân tập Vovinam.
                    <br />
                    Các phòng bắt đầu bằng DE thuộc tòa nhà Delta. VD: DE,..
                    <br />
                    Little UK (LUK) thuộc tầng 5 tòa nhà Delta
                </p>
                {/* Render year select */}
                {/* Render the table with data */}
                {/* Replace this table with the actual data provided in the HTML */}
                <table>
                    <thead>
                        <tr>
                            <th rowspan="2">
                                <span className="auto-style1">
                                    <strong>Year</strong>
                                </span>
                                <select
                                    value={selectedYear}
                                    onChange={(e) => setSelectedYear(e.target.value)}
                                >
                                    {generateYearOptions()}
                                </select>
                                <br />
                                Week
                                <select
                                    value={selectedWeek}
                                    onChange={(e) => {
                                        setSelectedWeek(e.target.value);
                                    }}
                                >
                                    {generateWeekOptions(selectedYear)}
                                </select>
                            </th>
                            <th align="center">Mon</th>
                            <th align="center">Tue</th>
                            <th align="center">Wed</th>
                            <th align="center">Thu</th>
                            <th align="center">Fri</th>
                            <th align="center">Sat</th>
                            <th align="center">Sun</th>
                        </tr>
                        {generateTableHeader(selectedWeek)}
                    </thead>
                    <tbody>{generateTableContent()}</tbody>
                </table>
                <p>
                    <b>More note / Chú thích thêm:</b>
                    <ul><li>(<font color="green">attended</font>): {name} had attended this activity / {name} đã tham gia hoạt động này</li>
                        <li>(<font color="red">absent</font>): {name} had NOT attended this activity / {name} đã vắng mặt buổi này</li>
                        <li>(<font color="red">null</font>): {name} had NOT registered yet / {name} chưa được tạo điểm danh </li>

                        <li>(-): no data was given / chưa có dữ liệu</li> </ul>
                </p>
                {/* More content */}
            </div>
        </div>

    );
}

export default WeeklyTimetable;
