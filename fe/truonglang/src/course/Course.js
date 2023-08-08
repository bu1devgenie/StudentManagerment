import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import Navbar from '../Navbar';
import './Course.css';
import Cookies from 'js-cookie';
import ReactPaginate from 'react-paginate';
import {Modal, Button} from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import AsyncSelect from 'react-select/async';
function Course() {
    const navigate = useNavigate();
    const [courseData, setCourseData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0); // Page index starts from 0
    const [totalPages, setTotalPages] = useState(0); // Total number of pages
    const [selectedRowData, setSelectedRowData] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showShutDownModal, setShowShutDownModal] = useState(false);
    const accessToken = Cookies.get('accessToken');
    const [searchData, setSearchData] = useState({
        name: '',
        totalslot: '',
        courseSemester: '',
        activity: true
    });

    // search handle 
    const handleSearchInputChange = (e) => {
        const {name, value} = e.target;
        setSearchData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };


    // load data course
    useEffect(() => {
        fetchCourse(currentPage);
    }, [currentPage]);

    const fetchCourse = async (page) => {
        try {
            let formDataSearch = new FormData();

            if (searchData.name) {
                formDataSearch.append('courseName', searchData.name);
            }
            if (searchData.totalslot) {
                formDataSearch.append('total_slot', Number(searchData.totalslot));
            }
            if (searchData.courseSemester) {
                formDataSearch.append('courseSemester', Number(searchData.courseSemester));
            }
            if (searchData.activity) {
                formDataSearch.append('activity', searchData.activity);
            }
            var url = `http://localhost:9999/course/searchCourse?targetPageNumber=${page}`;
            const response = await fetch(
                url,
                {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + accessToken
                    },
                    body: formDataSearch
                }
            );
            if (!response.ok) {
                throw new Error('Error fetching data');
            }
            const data = await response.json();
            setCourseData(data.content);
            setTotalPages(data.totalPages);
        } catch (error) {
            alert(error.message);
        }
    };
    // xử lí khi thay đổi trang
    const handlePageChange = ({selected}) => {
        setCurrentPage(selected);
    };
    // xử lí khi click update
    const handleUpdateClick = (rowData) => {
        console.log(rowData.course);
        setShowModal(true);
        setSelectedRowData(rowData.course);
    };
    // xử lí khi click delete
    const handleShutDownClick = (rowData) => {
        setShowShutDownModal(true);
        setSelectedRowData(rowData.course);
    };
    // xử lí khi click <add>
    const handleAddClick = () => {
        setShowAddModal(true);

    };

    // xử lí khi click search
    const handleSearchClick = (e) => {
        e.preventDefault();
        fetchCourse(0);
        setCurrentPage(0);
    };


    // render modal update
    const UpdateCourseModal = ({course, showModal}) => {
        const [formData, setFormData] = useState({
            // Initialize form data for the new course
            name: course.name,
            totalslot: course.totalSlot,
            courseSemester: course.courseSemester,
            activity: course.activity
        });

        const handleChange = (e) => {
            const {name, value} = e.target;
            setFormData((prevData) => ({
                ...prevData,
                [name]: value
            }));
        };

        // xử lí khi submit update
        const handleSubmit = (e) => {
            e.preventDefault();
            // Lấy các thông tin cần thiết từ formData
            let formDataUpdate = new FormData();
            formDataUpdate.append('courseName', formData.name);
            formDataUpdate.append('total_slot', Number(formData.totalslot));
            formDataUpdate.append('course_semester', Number(formData.courseSemester));
            formDataUpdate.append('activity', formData.activity);
            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/course/updateCourse', {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                },
                body: formDataUpdate
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Error fetching data');
                    }
                    return response;
                })
                .then((data) => {
                    console.log(data);
                    alert("update successfull");
                    setShowModal(false); // Đóng modal sau khi cập nhật thành công
                })
                .catch((error) => {
                    alert(error.message);
                });
        };
        const handleCloseModal = () => {
            setShowModal(false);
        };
        return (
            <Modal show={showModal} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Update Course</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>

                        <Form.Label htmlFor="courseSemester">Course Semester:</Form.Label>
                        <Form.Select id="courseSemester" name="courseSemester" onChange={handleChange}>
                            <option value="0">All Semesters</option>
                            {Array.from({length: 10}, (_, index) => index + 1).map((semester) => (
                                <option key={semester} value={semester} selected={semester === course.courseSemester}>
                                    {semester}
                                </option>
                            ))}
                        </Form.Select>

                        <Form.Label htmlFor="name" >Name:</Form.Label>
                        <Form.Control type="text" id="name" name="name" onChange={handleChange} value={course.name} />

                        <Form.Label htmlFor="totalslot" >Total Slot:</Form.Label>
                        <Form.Control type="text" id="totalslot" name="totalslot" onChange={handleChange} value={course.totalSlot} />


                        <Form.Label htmlFor="activity">Activity:</Form.Label>
                        <Form.Select id="activity" name="activity" onChange={handleChange}>
                            <option value="True" selected={course.activity === true}>Active</option>
                            <option value="True" selected={course.activity === false}>Stop</option>
                        </Form.Select>

                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Update
                    </Button>
                </Modal.Footer>
            </Modal >
        );
    };
    // render modal delete
    const ShutDownCourseModal = ({course, showShutDownModal}) => {
        const handleConfirmShutDown = () => {
            fetch('http://localhost:9999/course/shutdownCourse?name=' + course.name, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                },
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Error fetching data');
                    }
                    return response;
                })
                .then((data) => {
                    if (data) {
                        alert("Shut down successfull");
                    } else {
                        alert("Shut down failed");
                    }
                    setShowModal(false); // Đóng modal sau khi cập nhật thành công
                })
                .catch((error) => {
                    alert(error.message);
                });
            // After the delete action is completed, hide the modal
            setShowShutDownModal(false);
        };
        return (
            <Modal show={showShutDownModal} onHide={() => setShowShutDownModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm ShutDown</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Are you sure you want to delete this course?</p>
                    <p>Email: {course.name}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowShutDownModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleConfirmShutDown}>
                        Shut Down
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    };
    // render modal add
    const AddCourseModal = ({showModal}) => {
        const [formData, setFormData] = useState({
            // Initialize form data for the new course
            name: '',
            totalslot: '',
            courseSemester: '',
            activity: true
        });

        const handleChange = (e) => {
            const {name, value} = e.target;
            setFormData((prevData) => ({
                ...prevData,
                [name]: value,
            }));
        };



        // submit add
        const handleSubmit = (e) => {
            e.preventDefault();
            let formDataAdd = new FormData();
            formDataAdd.append('courseName', formData.name);
            formDataAdd.append('total_slot', Number(formData.totalslot));
            formDataAdd.append('course_semester', Number(formData.courseSemester));
            formDataAdd.append('activity', formData.activity);
            console.log(formData);
            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/course/addCourse', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                },
                body: formDataAdd
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Error fetching data');
                    }
                    return response.json();
                })
                .then((data) => {
                    alert("Add successfull");
                    setShowModal(false); // Đóng modal sau khi cập nhật thành công
                })
                .catch((error) => {
                    alert(error.message);
                });

            setShowAddModal(false);
        };

        const handleCloseModal = () => {
            setShowAddModal(false);
        };

        return (
            <Modal show={showAddModal} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Course</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>

                        <Form.Label htmlFor="courseSemester">Course Semester:</Form.Label>
                        <Form.Select id="courseSemester" name="courseSemester" onChange={handleChange}>
                            <option value="0">All Semesters</option>
                            {Array.from({length: 10}, (_, index) => index + 1).map((semester) => (
                                <option key={semester} value={semester}>
                                    {semester}
                                </option>
                            ))}
                        </Form.Select>

                        <Form.Label htmlFor="name" >Name:</Form.Label>
                        <Form.Control type="text" id="name" name="name" onChange={handleChange} />

                        <Form.Label htmlFor="totalslot" >Total Slot:</Form.Label>
                        <Form.Control type="text" id="totalslot" name="totalslot" onChange={handleChange} />


                        <Form.Label htmlFor="activity">Activity:</Form.Label>
                        <Form.Select id="activity" name="activity" onChange={handleChange}>
                            <option value="True">Active</option>
                            <option value="False">Stop</option>
                        </Form.Select>

                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Add Course
                    </Button>
                </Modal.Footer>
            </Modal >
        );
    };



    // Render the table only when there is data available
    const renderTable = () => {
        if (courseData.length === 0) {
            return <p>No data available.</p>; // Show a message when no data
        }
        return (
            <table className="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Course Semester</th>
                        <th>Name</th>
                        <th>Total Slot</th>
                        <th>Activity</th>
                        <th>Actions</th>
                        {/* Add more table headers for other course properties if needed */}
                    </tr>
                </thead>
                <tbody>
                    {courseData.map((course) => (
                        <tr key={course.id}>
                            <td>{course.id}</td>
                            <td>{course.courseSemester}</td>
                            <td>{course.name}</td>
                            <td>{course.totalSlot}</td>
                            <td>{course.activity ? 'Active' : 'Stop'}</td>
                            <td>
                                <button
                                    className="btn btn-primary"
                                    onClick={() => handleUpdateClick({course})}>
                                    Update
                                </button>
                                <button className="btn btn-danger"
                                    onClick={() => handleShutDownClick({course})}>
                                    Shut Down
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        );
    };
    return (
        <div>
            <Navbar />
            <div className="course-container">
                <h2>List of Course:</h2>
                {/* Render the table using the conditional function */}
                <button className="btn btn-primary" onClick={handleAddClick}>
                    Add New Course
                </button>
                {/* Search input fields */}
                {/* Search form */}
                <div>
                    <Form onSubmit={handleSearchClick} className="search-form">
                        <Form.Label>Search by Course Semester:</Form.Label>

                        <Form.Select id="courseSemester" name="courseSemester" onChange={handleSearchInputChange}>
                            <option value="0">All Semesters</option>
                            {Array.from({length: 10}, (_, index) => index + 1).map((semester) => (
                                <option key={semester} value={semester}>
                                    {semester}
                                </option>
                            ))}
                        </Form.Select>
                        <Form.Label>Search by Name:</Form.Label>
                        <Form.Control
                            type="text"
                            name="name"
                            placeholder="Enter name"
                            value={searchData.name}
                            onChange={handleSearchInputChange}
                        />
                        <Form.Label>Search by Total slot:</Form.Label>
                        <Form.Control
                            type="text"
                            name="totalslot"
                            placeholder="Enter total slot"
                            value={searchData.totalslot}
                            onChange={handleSearchInputChange}
                        />
                        <Form.Label>Search by Activity:</Form.Label>
                        <Form.Select id="activity" name="activity" onChange={handleSearchInputChange}>
                            <option value="True" >Active</option>
                            <option value="False" >Stop</option>
                        </Form.Select>


                        <Button variant="primary" type="submit">
                            Search
                        </Button>
                    </Form>
                </div>
                {renderTable()}
                {/* Pagination */}
                <div className="pagination-container">
                    <ReactPaginate
                        previousLabel={'Previous'}
                        nextLabel={'Next'}
                        breakLabel={'...'}
                        pageCount={totalPages}
                        marginPagesDisplayed={2}
                        pageRangeDisplayed={5}
                        onPageChange={handlePageChange}
                        previousClassName={'page-item'} // Add page-item class to Previous button
                        nextClassName={'page-item'} // Add page-item class to Next button
                        breakClassName={'page-item'}
                        pageClassName={'page-item'}
                        pageLinkClassName={'page-link'} // Add page-link class to page links
                        containerClassName={'pagination'}
                        activeClassName={'active'}
                    />
                </div>
                {/* Pagination */}
                {/* Modal update*/}
                {showModal && (
                    <UpdateCourseModal
                        course={selectedRowData}
                        showModal={showModal}
                    />
                )}
                {/* Modal delete*/}
                {showShutDownModal && (
                    <ShutDownCourseModal
                        course={selectedRowData}
                        showShutDownModal={showShutDownModal}
                    />
                )}
                {/* Modal add*/}
                {showAddModal && (
                    <AddCourseModal
                        showModal={showAddModal}
                    />
                )}
            </div>
        </div>
    );

}

export default Course;
