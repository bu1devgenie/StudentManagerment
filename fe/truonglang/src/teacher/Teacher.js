import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import Navbar from '../Navbar';
import './Teacher.css';
import Cookies from 'js-cookie';
import ReactPaginate from 'react-paginate';
import {Modal, Button} from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import AsyncSelect from 'react-select/async';
function Teacher() {
    const navigate = useNavigate();
    const [teacherData, setTeacherData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0); // Page index starts from 0
    const [totalPages, setTotalPages] = useState(0); // Total number of pages
    const [selectedRowData, setSelectedRowData] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const accessToken = Cookies.get('accessToken');
    const [searchData, setSearchData] = useState({
        msgv: '',
        name: '',
        dob: '',
        address: '',
        email: '',
        course: [],
    });

    // search handle 
    const handleSearchInputChange = (e) => {
        const {name, value} = e.target;
        setSearchData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };
    const handleSearchCourseChange = (selectedOption) => {
        setSearchData((prevData) => ({
            ...prevData,
            course: selectedOption,
        }));
    };

    // load data teacher
    useEffect(() => {
        fetchTeachers(currentPage);
    }, [currentPage]);
    const convertCourseListToStringList = (courseList) => {
        const stringList = courseList.map((course) => course.value);
        return stringList;
    };
    // load course from server
    const loadCourses = (inputValue, callback) => {
        fetch('http://localhost:9999/course/searchCourse?courseName=' + inputValue, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Error fetching data');
                }
                return response.json();
            })
            .then((data) => {
                const options = data.map((course) => ({
                    value: course.name, // Use a unique identifier as the value
                    label: course.name, // Use the course name as the label
                }));
                callback(options);
            })
            .catch((error) => {
                alert(error.message);
            });
    };
    // load emails from server
    const loadEmails = (inputValue, callback) => {
        fetch('http://localhost:9999/account/searchEmailNoConnected?email=' + inputValue, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Error fetching data');
                }
                return response.json();
            })
            .then((data) => {
                const options = data.map((account) => ({
                    value: account, // Use a unique identifier as the value
                    label: account, // Use the course name as the label
                }));
                callback(options);
            })
            .catch((error) => {
                alert(error.message);
            });
    };
    const fetchTeachers = async (page) => {
        try {
            let formDataSearch = new FormData();
            formDataSearch.append('email', searchData.email);
            formDataSearch.append('msgv', searchData.msgv);
            formDataSearch.append('name', searchData.name);
            formDataSearch.append('address', searchData.address);
            formDataSearch.append('dob', searchData.dob);
            const courseListAsString = convertCourseListToStringList(searchData.course);
            formDataSearch.append('course', courseListAsString);
            var url = `http://localhost:9999/teacher/searchTeacher?targetPageNumber=${page}`;

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
            setTeacherData(data.content); // Update the teacherData state with the new data
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
        setShowModal(true);
        setSelectedRowData(rowData.teacher); // Pass the teacher data directly
    };
    // xử lí khi click delete
    const handleDeleteClick = (rowData) => {
        setSelectedRowData(rowData.teacher);
        setShowDeleteModal(true);
    };
    // xử lí khi click add
    const handleAddClick = () => {
        setShowAddModal(true);

    };

    // xử lí khi click search
    const handleSearchClick = (e) => {
        e.preventDefault();
        fetchTeachers(0);
        console.log("clicked search");
    };


    // render modal update
    const UpdateTeacherModal = ({teacher, showModal}) => {
        const [formData, setFormData] = useState(teacher);
        const handleChange = (e) => {
            const {name, value} = e.target;
            setFormData((prevData) => ({
                ...prevData,
                [name]: value
            }));
        };
        // xử lí khi thay đổi lựa chon course
        const handleCourseChange = (selectedOption) => {
            setFormData((prevData) => ({
                ...prevData,
                course: selectedOption
            }));
        };
        // xử lí khi thay đổi lựa chon course
        const handleEmailsChange = (selectedOption) => {
            setFormData((prevData) => ({
                ...prevData,
                email: selectedOption
            }));
        };

        // xử lí khi submit update
        const handleSubmit = (e) => {
            e.preventDefault();
            // Lấy các thông tin cần thiết từ formData
            let formDataUpdate = new FormData();
            if (formData.email) {
                formDataUpdate.append('email', formData.email.value);
            }
            console.log(formData.email);

            formDataUpdate.append('msgvUpdate', formData.msgv);
            formDataUpdate.append('name', formData.name);
            formDataUpdate.append('address', formData.address);
            formDataUpdate.append('dob', formData.dob);
            formDataUpdate.append('gender', formData.gender);

            const courseListAsString = convertCourseListToStringList(formData.course);
            formDataUpdate.append('course', courseListAsString);


            const fileInput = document.getElementById('avatarUpdate');
            const file = fileInput.files[0];
            formDataUpdate.append('avatar', file);


            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/teacher/updateTeacher', {
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
                    return response.json();
                })
                .then((data) => {
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
                    <Modal.Title>Update Teacher</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <div >
                            <Form.Label htmlFor="id" >ID:</Form.Label>
                            <Form.Control readOnly type="text" id="id" name="id" value={formData.id} onChange={handleChange} />
                        </div>

                        <Form.Label htmlFor="msgv">MSGV:</Form.Label>
                        <Form.Control readOnly type="text" id="msgv" name="msgv" value={formData.msgv} onChange={handleChange} />

                        <Form.Label htmlFor="name">Name:</Form.Label>
                        <Form.Control type="text" id="name" name="name" value={formData.name} onChange={handleChange} />

                        <Form.Label htmlFor="dob">Dob:</Form.Label>
                        <Form.Control type="date" id="dob" name="dob" value={formData.dob} onChange={handleChange} />

                        <Form.Label htmlFor="gender">Gender:</Form.Label>
                        <Form.Select id="gender" name="gender" onChange={handleChange}>
                            <option value="MALE" selected={formData.gender === 'MALE'}>Male</option>
                            <option value="FEMALE" selected={formData.gender === 'FEMALE'}>Female</option>
                            <option value="OTHER" selected={formData.gender === 'OTHER'}>Other</option>
                        </Form.Select>


                        <Form.Label htmlFor="address">Address:</Form.Label>
                        <Form.Control type="text" id="address" name="address" value={formData.address} onChange={handleChange} />

                        <Form.Label htmlFor="email">Email:</Form.Label>
                        <AsyncSelect
                            cacheOptions
                            defaultOptions
                            loadOptions={loadEmails}
                            isMulti={false}
                            onChange={handleEmailsChange}
                            defaultInputValue={formData.email}
                        />

                        <Form.Label htmlFor="course">Course:</Form.Label>
                        <AsyncSelect
                            cacheOptions
                            defaultOptions
                            loadOptions={loadCourses}
                            isMulti={true}
                            onChange={handleCourseChange}
                        />

                        <Form.Label htmlFor="avatarUpdate">Avatar:</Form.Label>
                        <Form.Control type="file" id="avatarUpdate" name="avatarUpdate" onChange={handleChange} />
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
    const DeleteTeacherModal = ({teacher, showDeleteModal}) => {
        const [teacherData, setTeacherData] = useState(teacher);


        const handleConfirmDelete = () => {
            fetch('http://localhost:9999/teacher/deleteTeacher?msgv=' + teacherData.msgv, {
                method: 'Delete',
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
                        alert("delete successfull");
                    } else {
                        alert("delete failed");
                    }
                    setShowModal(false); // Đóng modal sau khi cập nhật thành công
                })
                .catch((error) => {
                    alert(error.message);
                });
            // After the delete action is completed, hide the modal
            setShowDeleteModal(false);
        };
        return (
            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Delete</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Are you sure you want to delete this teacher?</p>
                    <p>Msgv: {teacherData.msgv}</p>
                    <p>Name: {teacherData.name}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleConfirmDelete}>
                        Confirm Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    };
    // render modal add
    const AddTeacherModal = ({showModal}) => {
        const [formData, setFormData] = useState({
            // Initialize form data for the new teacher
            name: '',
            dob: '',
            gender: 'MALE',
            address: '',
            email: '',
            course: [],
            avatar: null
        });
        const handleFileChange = (e) => {
            const file = e.target.files[0];
            setFormData((prevData) => ({
                ...prevData,
                avatar: file,
            }));
        };
        const handleChange = (e) => {
            const {name, value} = e.target;
            setFormData((prevData) => ({
                ...prevData,
                [name]: value,
            }));
        };

        const handleCourseChange = (selectedOption) => {
            setFormData((prevData) => ({
                ...prevData,
                course: selectedOption,
            }));
        };
        // submit add
        const handleSubmit = (e) => {
            e.preventDefault();

            let formDataAdd = new FormData();
            formDataAdd.append('name', formData.name);
            formDataAdd.append('address', formData.address);
            formDataAdd.append('dob', formData.dob);
            formDataAdd.append('email', formData.email.value);
            formDataAdd.append('gender', formData.gender);

            const courseListAsString = convertCourseListToStringList(formData.course);
            formDataAdd.append('course', courseListAsString);


            const fileInput = document.getElementById('avatarAdd');
            const file = fileInput.files[0];
            formDataAdd.append('avatar', file);
            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/teacher/addTeacher', {
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
        const handleEmailsChange = (selectedOption) => {
            setFormData((prevData) => ({
                ...prevData,
                email: selectedOption
            }));
        };
        const handleCloseModal = () => {
            setShowAddModal(false);
        };

        return (
            <Modal show={showAddModal} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Update Teacher</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <div >
                            <Form.Label htmlFor="id" >ID:</Form.Label>
                            <Form.Control readOnly type="text" id="id" name="id" onChange={handleChange} />
                        </div>

                        <Form.Label htmlFor="msgv">MSGV:</Form.Label>
                        <Form.Control readOnly type="text" id="msgv" name="msgv" onChange={handleChange} />

                        <Form.Label htmlFor="name">Name:</Form.Label>
                        <Form.Control type="text" id="name" name="name" onChange={handleChange} />

                        <Form.Label htmlFor="dob">Dob:</Form.Label>
                        <Form.Control type="date" id="dob" name="dob" onChange={handleChange} />

                        <Form.Label htmlFor="gender">Gender:</Form.Label>
                        <Form.Select id="gender" name="gender" onChange={handleChange}>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                            <option value="OTHER">Other</option>
                        </Form.Select>

                        <Form.Label htmlFor="address">Address:</Form.Label>
                        <Form.Control type="text" id="address" name="address" onChange={handleChange} />

                        <Form.Label htmlFor="email">Email:</Form.Label>
                        <AsyncSelect
                            cacheOptions
                            defaultOptions
                            loadOptions={loadEmails}
                            isMulti={false}
                            onChange={handleEmailsChange}
                        />

                        <Form.Label htmlFor="course">Course:</Form.Label>
                        <AsyncSelect
                            cacheOptions
                            defaultOptions
                            loadOptions={loadCourses}
                            isMulti={true}
                            onChange={handleCourseChange}
                        />

                        <Form.Label htmlFor="avatarAdd">Avatar:</Form.Label>
                        <Form.Control type="file" id="avatarAdd" name="avatarAdd" onChange={handleFileChange} />
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Add Teacher
                    </Button>
                </Modal.Footer>
            </Modal >
        );
    };



    // Render the table only when there is data available
    const renderTable = () => {
        if (teacherData.length === 0) {
            return <p>No data available.</p>; // Show a message when no data
        }
        return (
            <table className="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Avatar</th>
                        <th>MSGV</th>
                        <th>Name</th>
                        <th>Dob</th>
                        <th>Gender</th>
                        <th>Address</th>
                        <th>Email</th>
                        <th>Course</th>
                        <th>Actions</th>
                        {/* Add more table headers for other teacher properties if needed */}
                    </tr>
                </thead>
                <tbody>
                    {teacherData.map((teacher) => (
                        <tr key={teacher.id}>
                            <td>{teacher.id}</td>
                            <td>
                                {teacher.avatar && (
                                    <img
                                        src={teacher.avatar}
                                        alt="Avatar"
                                        style={{maxWidth: '100px', maxHeight: '100px'}}
                                    />
                                )}
                            </td>
                            <td>{teacher.msgv}</td>
                            <td>{teacher.name}</td>
                            <td>{teacher.dob}</td>
                            <td>{teacher.gender}</td>
                            <td>{teacher.address}</td>
                            <td>{teacher.email}</td>
                            <td>
                                <ul>
                                    {teacher.course.map((course) => (
                                        <li key={course.id}>{course.name}</li>
                                    ))}
                                </ul>
                            </td>
                            <td>
                                <button
                                    className="btn btn-primary"
                                    onClick={() => handleUpdateClick({teacher})}>
                                    Update
                                </button>
                                <button className="btn btn-danger"
                                    onClick={() => handleDeleteClick({teacher})}>
                                    Delete
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
            <div className="teacher-container">
                <h2>List of Teachers:</h2>
                {/* Render the table using the conditional function */}
                <button className="btn btn-primary" onClick={handleAddClick}>
                    Add New Teacher
                </button>
                {/* Search input fields */}
                {/* Search form */}
                <div>
                    <Form onSubmit={handleSearchClick} className="search-form">
                        <Form.Label>Search by MSGV:</Form.Label>
                        <Form.Control
                            type="text"
                            name="msgv"
                            placeholder="Enter MSGV"
                            value={searchData.msgv}
                            onChange={handleSearchInputChange}
                        />

                        <Form.Label>Search by Name:</Form.Label>
                        <Form.Control
                            type="text"
                            name="name"
                            placeholder="Enter Name"
                            value={searchData.name}
                            onChange={handleSearchInputChange}
                        />

                        <Form.Label>Search by Dob:</Form.Label>
                        <Form.Control
                            type="date"
                            name="dob"
                            placeholder="Enter Dob"
                            value={searchData.dob}
                            onChange={handleSearchInputChange}
                        />

                        <Form.Label>Search by Address:</Form.Label>
                        <Form.Control
                            type="text"
                            name="address"
                            placeholder="Enter Address"
                            value={searchData.address}
                            onChange={handleSearchInputChange}
                        />

                        <Form.Label>Search by Email:</Form.Label>
                        <Form.Control
                            type="text"
                            name="email"
                            placeholder="Enter Email"
                            value={searchData.email}
                            onChange={handleSearchInputChange}
                        />

                        <Form.Label>Search by Course:</Form.Label>
                        <AsyncSelect
                            cacheOptions
                            defaultOptions
                            loadOptions={loadCourses}
                            isMulti={true}
                            onChange={handleSearchCourseChange}
                            defaultInputValue={searchData.course}
                        />

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
                    <UpdateTeacherModal
                        teacher={selectedRowData}
                        showModal={showModal}
                    />
                )}
                {/* Modal delete*/}
                {showDeleteModal && (
                    <DeleteTeacherModal
                        teacher={selectedRowData}
                        showDeleteModal={showDeleteModal}
                    />
                )}
                {/* Modal add*/}
                {showAddModal && (
                    <AddTeacherModal
                        showModal={showAddModal}
                    />
                )}
            </div>
        </div>
    );

}

export default Teacher;
