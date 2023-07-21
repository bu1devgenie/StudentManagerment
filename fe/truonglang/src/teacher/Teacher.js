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
    const [courseData, setCourseData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0); // Page index starts from 0
    const [totalPages, setTotalPages] = useState(0); // Total number of pages
    const [showModal, setShowModal] = useState(false);
    const [selectedRowData, setSelectedRowData] = useState(null);

    const accessToken = Cookies.get('accessToken');
    // load data teacher
    useEffect(() => {
        fetchTeachers(currentPage);
    }, [currentPage]);
    // check accessToken is valid
    useEffect(() => {
        // Kiểm tra có AccessToken trong cookie hay không
        const accessToken = Cookies.get('accessToken');
        if (accessToken) {
            // Gọi server để kiểm tra access token
            fetch('http://localhost:9999/checkAccessToken', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + accessToken
                },
            })
                .then((response) => {
                    if (response.status === 200) {
                        // Access token is valid, proceed to fetch teachers data
                    } else {
                        throw new Error('Hết phiên đăng nhập');
                    }
                })
                .catch((error) => {
                    alert(error.message);
                    navigate('/login'); // Redirect to login page
                });
        } else {
            navigate('/login'); // Redirect to login page if access token is not available
        }
    }, [navigate, accessToken]);
    const fetchTeachers = async (page) => {
        try {
            const response = await fetch(
                `http://localhost:9999/teacher/searchTeacher?type=&searchText&targetPageNumber=${page}`,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + accessToken
                    }
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
        console.log("update");
        console.log("showModal: " + showModal);
        console.log("selectedRowData: " + selectedRowData);

        setShowModal(true);
        setSelectedRowData(rowData);
    };
    const handleTeacherUpdate = async (updatedData) => {
        console.log("Teacher data to be updated:", updatedData);
        setShowModal(false);
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
                                <button className="btn btn-danger">Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        );
    };
    // render modal 
    const UpdateTeacherModal = ({teacher}) => {
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
        // load course from server
        const loadCourses = (inputValue, callback) => {
            // Thay thế bằng mã gọi API để tìm kiếm các khóa học dựa trên từ khóa (inputValue)
            // Ví dụ:
            // fetch('http://example.com/api/courses?search=' + inputValue)
            //   .then(response => response.json())
            //   .then(data => callback(data));
        };
        // xử lí khi submit update
        const handleSubmit = (e) => {
            e.preventDefault();
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
                            <Form.Control type="text" id="id" name="id" value={formData.id} onChange={handleChange} />
                        </div>

                        <Form.Label htmlFor="msgv">MSGV:</Form.Label>
                        <Form.Control type="text" id="msgv" name="msgv" value={formData.msgv} onChange={handleChange} />

                        <Form.Label htmlFor="name">Name:</Form.Label>
                        <Form.Control type="text" id="name" name="name" value={formData.name} onChange={handleChange} />

                        <Form.Label htmlFor="dob">Dob:</Form.Label>
                        <Form.Control type="date" id="dob" name="dob" value={formData.dob} onChange={handleChange} />

                        <Form.Label htmlFor="gender">Gender:</Form.Label>
                        <Form.Select id="gender" name="gender" value={formData.gender} onChange={handleChange}>
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                            <option value="other">Other</option>
                        </Form.Select>

                        <Form.Label htmlFor="address">Address:</Form.Label>
                        <Form.Control type="text" id="address" name="address" value={formData.address} onChange={handleChange} />

                        <Form.Label htmlFor="email">Email:</Form.Label>
                        <Form.Control type="email" id="email" name="email" value={formData.email} onChange={handleChange} />

                        <Form.Label htmlFor="course">Course:</Form.Label>
                        <AsyncSelect
                            cacheOptions
                            defaultOptions
                            loadOptions={loadCourses}
                            isMulti={true}
                            onChange={handleCourseChange}
                        />

                        <Form.Label htmlFor="avatar">Avatar:</Form.Label>
                        <Form.Control type="file" id="avatar" name="avatar" value={formData.avatar} onChange={handleChange} />
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
    return (
        <div>
            <Navbar />
            <div className="teacher-container">
                <h2>List of Teachers:</h2>
                {/* Render the table using the conditional function */}
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
                {/* Modal */}
                {showModal && (
                    <UpdateTeacherModal
                        teacher={selectedRowData}
                        onUpdate={handleTeacherUpdate}
                        showModal={showModal}
                    />
                )}
            </div>

        </div>
    );

}

export default Teacher;
