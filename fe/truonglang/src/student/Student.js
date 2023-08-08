import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import Navbar from '../Navbar';
import './Student.css';
import Cookies from 'js-cookie';
import ReactPaginate from 'react-paginate';
import {Modal, Button} from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import AsyncSelect from 'react-select/async';
function Student() {
    const navigate = useNavigate();
    const [studentData, setStudentData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0); // Page index starts from 0
    const [totalPages, setTotalPages] = useState(0); // Total number of pages
    const [selectedRowData, setSelectedRowData] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const accessToken = Cookies.get('accessToken');
    const [searchData, setSearchData] = useState({
        mssv: '',
        semester: null,
        name: '',
        dob: null,
        email: ''
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

    // load data student
    useEffect(() => {
        fetchStudent(currentPage);
    }, [currentPage]);


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
    const fetchStudent = async (page) => {
        try {
            let formDataSearch = new FormData();
            if (searchData.mssv) {
                formDataSearch.append('mssv', searchData.mssv);
            }
            if (searchData.name) {
                formDataSearch.append('name', searchData.name);
            }
            if (searchData.email) {
                formDataSearch.append('email', searchData.email);
            }
            if (searchData.semester) {
                formDataSearch.append('semester', Number(searchData.semester));
            }
            if (searchData.dob) {
                formDataSearch.append('dob', searchData.dob);
            }
            var url = `http://localhost:9999/student/searchStudent?targetPageNumber=${page}`;
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
            setStudentData(data.content);
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
        console.log(rowData.student);
        setShowModal(true);
        setSelectedRowData(rowData.student);
    };
    // xử lí khi click delete
    const handleDeleteClick = (rowData) => {
        setShowDeleteModal(true);
        setSelectedRowData(rowData.student);
    };
    // xử lí khi click <add></add>
    const handleAddClick = () => {
        setShowAddModal(true);

    };

    // xử lí khi click search
    const handleSearchClick = (e) => {
        e.preventDefault();
        fetchStudent(0);
        setCurrentPage(0);
    };


    // render modal update
    const UpdateStudentModal = ({student, showModal}) => {
        const [formData, setFormData] = useState(student);
        const handleChange = (e) => {
            const {name, value} = e.target;
            setFormData((prevData) => ({
                ...prevData,
                [name]: value
            }));
        };

        // xử lí khi thay đổi lựa chon email
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

            formDataUpdate.append('mssvUpdate', formData.mssv);
            formDataUpdate.append('semester', formData.semester);
            formDataUpdate.append('name', formData.name);
            formDataUpdate.append('dob', formData.dob);
            formDataUpdate.append('address', formData.address);
            formDataUpdate.append('gender', formData.gender);
            const fileInput = document.getElementById('avatarUpdate');
            const file = fileInput.files[0];
            formDataUpdate.append('avatar', file);


            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/student/updateStudent', {
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
                    <Modal.Title>Update Student</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <div >
                            <Form.Label htmlFor="id" >ID:</Form.Label>
                            <Form.Control readOnly type="text" id="id" name="id" value={formData.id} onChange={handleChange} />
                        </div>

                        <Form.Label htmlFor="mssv">MSSV:</Form.Label>
                        <Form.Control readOnly type="text" id="mssv" name="mssv" value={formData.mssv} onChange={handleChange} />

                        <Form.Label htmlFor="semester">Semester:</Form.Label>
                        <Form.Select id="semester" name="semester" onChange={handleChange}>
                            <option value="0">All Semesters</option>
                            {Array.from({length: 10}, (_, index) => index + 1).map((semester) => (
                                <option key={semester} value={semester} selected={semester === formData.currentSemester}>
                                    {semester}
                                </option>
                            ))}
                        </Form.Select>

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
    const DeleteTeacherModal = ({student, showDeleteModal}) => {
        const handleConfirmDelete = () => {
            fetch('http://localhost:9999/student/deleteStudent?mssv=' + student.mssv, {
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
                    <p>Are you sure you want to delete this student?</p>
                    <p>Mssv: {student.mssv}</p>
                    <p>Name: {student.name}</p>
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
            // Initialize form data for the new student
            name: '',
            dob: '',
            semester: '',
            gender: 'MALE',
            address: '',
            email: '',
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
            formDataAdd.append('current_semester', formData.semester);
            formDataAdd.append('address', formData.address);
            formDataAdd.append('dob', formData.dob);
            formDataAdd.append('email', formData.email.value);
            formDataAdd.append('gender', formData.gender);
            const fileInput = document.getElementById('avatarAdd');
            const file = fileInput.files[0];
            formDataAdd.append('avatarFile', file);
            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/student/addStudent', {
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
                    <Modal.Title>Update Student</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <div >
                            <Form.Label htmlFor="id" >ID:</Form.Label>
                            <Form.Control readOnly type="text" id="id" name="id" onChange={handleChange} />
                        </div>

                        <Form.Label htmlFor="mssv">MSSV:</Form.Label>
                        <Form.Control readOnly type="text" id="mssv" name="mssv" onChange={handleChange} />

                        <Form.Label htmlFor="semester">Semester:</Form.Label>
                        <Form.Select id="semester" name="semester" onChange={handleChange}>
                            <option value="0">All Semesters</option>
                            {Array.from({length: 10}, (_, index) => index + 1).map((semester) => (
                                <option key={semester} value={semester}>
                                    {semester}
                                </option>
                            ))}
                        </Form.Select>

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

                        <Form.Label htmlFor="avatarAdd">Avatar:</Form.Label>
                        <Form.Control type="file" id="avatarAdd" name="avatarAdd" onChange={handleFileChange} />
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Add Student
                    </Button>
                </Modal.Footer>
            </Modal >
        );
    };



    // Render the table only when there is data available
    const renderTable = () => {
        if (studentData.length === 0) {
            return <p>No data available.</p>; // Show a message when no data
        }
        return (
            <table className="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Semester</th>
                        <th>Avatar</th>
                        <th>MSSV</th>
                        <th>Name</th>
                        <th>Dob</th>
                        <th>Gender</th>
                        <th>Address</th>
                        <th>Email</th>
                        <th>Actions</th>
                        {/* Add more table headers for other student properties if needed */}
                    </tr>
                </thead>
                <tbody>
                    {studentData.map((student) => (
                        <tr key={student.id}>
                            <td>{student.id}</td>
                            <td>{student.currentSemester}</td>
                            <td>
                                {student.avatar && (
                                    <img
                                        src={student.avatar}
                                        alt="Avatar"
                                        style={{maxWidth: '100px', maxHeight: '100px'}}
                                    />
                                )}
                            </td>
                            <td>{student.mssv}</td>
                            <td>{student.name}</td>
                            <td>{student.dob}</td>
                            <td>{student.gender}</td>
                            <td>{student.address}</td>
                            <td>{student.email}</td>
                            <td>
                                <button
                                    className="btn btn-primary"
                                    onClick={() => handleUpdateClick({student})}>
                                    Update
                                </button>
                                <button className="btn btn-danger"
                                    onClick={() => handleDeleteClick({student})}>
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
            <div className="student-container">
                <h2>List of Student:</h2>
                {/* Render the table using the conditional function */}
                <button className="btn btn-primary" onClick={handleAddClick}>
                    Add New Student
                </button>
                {/* Search input fields */}
                {/* Search form */}
                <div>
                    <Form onSubmit={handleSearchClick} className="search-form">
                        <Form.Label>Search by MSSV:</Form.Label>
                        <Form.Control
                            type="text"
                            name="mssv"
                            placeholder="Enter MSSV"
                            value={searchData.mssv}
                            onChange={handleSearchInputChange}
                        />
                        <Form.Label>Search by Semester:</Form.Label>
                        <Form.Control
                            type="text"
                            name="semester"
                            placeholder="Enter Semester"
                            value={searchData.semester}
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
                        <Form.Label>Search by Email:</Form.Label>
                        <Form.Control
                            type="text"
                            name="email"
                            placeholder="Enter Email"
                            value={searchData.email}
                            onChange={handleSearchInputChange}
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
                    <UpdateStudentModal
                        student={selectedRowData}
                        showModal={showModal}
                    />
                )}
                {/* Modal delete*/}
                {showDeleteModal && (
                    <DeleteTeacherModal
                        student={selectedRowData}
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

export default Student;
