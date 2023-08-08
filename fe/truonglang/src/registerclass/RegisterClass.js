import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import Navbar from '../Navbar';
import './RegisterClass.css';
import Cookies from 'js-cookie';
import ReactPaginate from 'react-paginate';
import {Modal, Button} from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import AsyncSelect from 'react-select/async';

function RegisterClass() {
    const navigate = useNavigate();
    const accessToken = Cookies.get('accessToken');
    const email = Cookies.get('email');
    const name = Cookies.get('name');
    const ms = Cookies.get('ms');
    const roles = Cookies.get('roles');
    const [isCheckboxChecked, setIsCheckboxChecked] = useState(false);

    const [formData, setFormData] = useState({
        courseName: '',
        class: '',
    });
    const handleClassChange = (selectedOption) => {
        setFormData((prevData) => ({
            ...prevData,
            class: selectedOption,
        }));
    };
    useEffect(() => {
        loadClass();
    }, [isCheckboxChecked]);
    const loadClass = async () => {
        try {
            const response = await fetch('http://localhost:9999/classroom/getClassRoomWithCourse?courseName=' + formData.courseName, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + accessToken,
                },
            });

            if (!response.ok) {
                throw new Error('Error fetching data');
            }

            const data = await response.json();
            const options = data.map((item) => ({
                value: item,
                label: item,
            }));
            console.log(options);
            return options;
        } catch (error) {
            alert(error.message);
            return [];
        }
    };


    const checkCourse = async () => {
        try {
            const response = await fetch('http://localhost:9999/course/checkCourse?courseName=' + formData.courseName, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + accessToken,
                },
            });
            if (!response.ok) {
                throw new Error('Error fetching data');
            }
            const data = await response.json();
            return data;
        } catch (error) {
            return false;
        }
    };

    const handleCheckboxChange = async (event) => {
        if (event.target.checked) {
            const apiResponse = await checkCourse();
            if (apiResponse === true) {
                setIsCheckboxChecked(true);
            } else {
                setIsCheckboxChecked(false);
            }
        } else {
            setFormData((prevData) => ({
                ...prevData,
                class: [],
            }));
            setIsCheckboxChecked(false);
        }
    };



    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    return (
        <div>
            <Navbar />
            <div>
                <table>
                    <tbody>
                        <tr style={{borderBottom: '0 none'}}>
                            <td>
                                <div>
                                    <h2>
                                        <span>Đăng ký học (Register for learning)</span>
                                    </h2>
                                    <br />
                                    <span id="" style={{color: 'Red'}}></span>
                                    <table>
                                        <tbody>
                                            <tr>
                                                <td>Mã môn học (SubjectCode)</td>
                                                <td>
                                                    <input name="courseName" type="text" value={formData.courseName} onChange={handleInputChange} disabled={isCheckboxChecked} />
                                                </td>
                                                <td>
                                                    <input type="checkbox" checked={isCheckboxChecked} onChange={handleCheckboxChange} />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Lớp (Class)</td>
                                                <td>
                                                    <AsyncSelect
                                                        cacheOptions
                                                        loadOptions={loadClass}
                                                        value={formData.class}
                                                        isMulti={false}
                                                        onChange={handleClassChange}
                                                        isDisabled={!isCheckboxChecked}
                                                    />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Thời gian học (TranningTime)</td>

                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default RegisterClass;
