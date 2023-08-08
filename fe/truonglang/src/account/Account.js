import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import Navbar from '../Navbar';
import './Account.css';
import Cookies from 'js-cookie';
import ReactPaginate from 'react-paginate';
import {Modal, Button} from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import AsyncSelect from 'react-select/async';
function Account() {
    const navigate = useNavigate();
    const [accountData, setAccountData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0); // Page index starts from 0
    const [totalPages, setTotalPages] = useState(0); // Total number of pages
    const [selectedRowData, setSelectedRowData] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const accessToken = Cookies.get('accessToken');
    const [searchData, setSearchData] = useState({
        email: '',
    });

    // search handle 
    const handleSearchInputChange = (e) => {
        const {name, value} = e.target;
        setSearchData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };


    // load data account
    useEffect(() => {
        fetchAccount(currentPage);
    }, [currentPage]);

    const fetchAccount = async (page) => {
        try {
            let formDataSearch = new FormData();

            if (searchData.email) {
                formDataSearch.append('email', searchData.email);
            }
            var url = `http://localhost:9999/account/searchAccount?targetPageNumber=${page}`;
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
            setAccountData(data.content);
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
        console.log(rowData.account);
        setShowModal(true);
        setSelectedRowData(rowData.account);
    };
    // xử lí khi click delete
    const handleDeleteClick = (rowData) => {
        setShowDeleteModal(true);
        setSelectedRowData(rowData.account);
    };
    // xử lí khi click <add></add>
    const handleAddClick = () => {
        setShowAddModal(true);

    };

    // xử lí khi click search
    const handleSearchClick = (e) => {
        e.preventDefault();
        fetchAccount(0);
        setCurrentPage(0);
    };


    // render modal update
    const UpdateAccountModal = ({account, showModal}) => {
        const [formData, setFormData] = useState({
            // Initialize form data for the new account
            email: account.email,
            password: account.password,
            roles: account.role,
        });
        const convertRolesListToStringList = (courseList) => {
            const stringList = courseList.map((course) => course.value);
            return stringList;
        };
        useEffect(() => {
            const selectedRoles = account.role.map((role) => ({
                value: role.name,
                label: role.name,
            }));
            setFormData((prevData) => ({
                ...prevData,
                roles: selectedRoles,
            }));
        }, [account]);
        const loadRoles = async () => {
            try {
                const response = await fetch('http://localhost:9999/account/allRoleForAccount', {
                    method: 'Get',
                    headers: {
                        'Authorization': 'Bearer ' + accessToken
                    },
                });

                if (!response.ok) {
                    throw new Error('Error fetching data');
                }

                const data = await response.json();
                const options = data.map((role) => ({
                    value: role.name, // Use a unique identifier as the value
                    label: role.name, // Use the course name as the label
                }));
                return options;
            } catch (error) {
                alert(error.message);
                return [];
            }
        };
        const handleChange = (e) => {
            const {name, value} = e.target;
            setFormData((prevData) => ({
                ...prevData,
                [name]: value
            }));
        };
        const handleRolesChange = (selectedOption) => {
            setFormData((prevData) => ({
                ...prevData,
                roles: selectedOption,
            }));
        };
        const selectedRoles = account.role.map((role) => ({
            value: role.name,
            label: role.name,
        }));
        const handleResetPassword = (e) => {
            e.preventDefault();
            let formDataUpdate = new FormData();
            formDataUpdate.append('email', formData.email);

            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/account/resetPassword', {
                method: 'POST',
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
                    console.log(data);
                    if (data === true) {
                        alert("reset successfull");
                    } else {
                        alert("reset failed");

                    }

                    setShowModal(false); // Đóng modal sau khi cập nhật thành công
                })
                .catch((error) => {
                    alert(error.message);
                });
        };
        // xử lí khi submit update
        const handleSubmit = (e) => {
            e.preventDefault();
            // Lấy các thông tin cần thiết từ formData
            let formDataUpdate = new FormData();
            formDataUpdate.append('email', formData.email);
            formDataUpdate.append('password', formData.password);
            const rolesListAsString = convertRolesListToStringList(formData.roles);
            formDataUpdate.append('lsRole', rolesListAsString);

            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/account/updateAccount', {
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
                    <Modal.Title>Update Account</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <div >
                            <Form.Label htmlFor="email" >Email:</Form.Label>
                            <Form.Control readOnly type="text" id="email" name="email" value={formData.email} onChange={handleChange} />
                        </div>

                        <Form.Label htmlFor="password">Password:</Form.Label>
                        <Form.Control type="password" id="password" name="password" value={formData.password} onChange={handleChange} />
                        <Form.Label htmlFor="roles">Roles:</Form.Label>
                        <AsyncSelect
                            cacheOptions
                            defaultOptions
                            loadOptions={loadRoles}
                            value={formData.roles} // Use the formData.roles as the value
                            isMulti={true}
                            onChange={handleRolesChange}
                        />
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleResetPassword}>
                        Reset Password
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Update
                    </Button>
                </Modal.Footer>
            </Modal >
        );
    };
    // render modal delete
    const DeleteAccountModal = ({account, showDeleteModal}) => {
        const handleConfirmDelete = () => {
            fetch('http://localhost:9999/account/deleteAccount?email=' + account.email, {
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
                    <p>Are you sure you want to delete this account?</p>
                    <p>Email: {account.email}</p>
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
    const AddAccountModal = ({showModal}) => {
        const [formData, setFormData] = useState({
            // Initialize form data for the new account
            email: '',
            password: '',
            roles: []
        });
        const loadRoles = async () => {
            try {
                const response = await fetch('http://localhost:9999/account/allRoleForAccount', {
                    method: 'Get',
                    headers: {
                        'Authorization': 'Bearer ' + accessToken
                    },
                });

                if (!response.ok) {
                    throw new Error('Error fetching data');
                }

                const data = await response.json();
                const options = data.map((role) => ({
                    value: role.name, // Use a unique identifier as the value
                    label: role.name, // Use the course name as the label
                }));
                return options;
            } catch (error) {
                alert(error.message);
                return [];
            }
        };
        const handleChange = (e) => {
            const {name, value} = e.target;
            setFormData((prevData) => ({
                ...prevData,
                [name]: value,
            }));
        };

        const handleRolesChange = (selectedOption) => {
            setFormData((prevData) => ({
                ...prevData,
                roles: selectedOption,
            }));
        };
        const convertRolesListToStringList = (courseList) => {
            const stringList = courseList.map((course) => course.value);
            return stringList;
        };
        // submit add
        const handleSubmit = (e) => {
            e.preventDefault();
            let formDataAdd = new FormData();
            formDataAdd.append('email', formData.email);
            const rolesListAsString = convertRolesListToStringList(formData.roles);
            formDataAdd.append('lsRole', rolesListAsString);

            // Gửi yêu cầu cập nhật đến server
            fetch('http://localhost:9999/account/addAccount', {
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
                    <Modal.Title>Add Account</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <div >
                            <Form.Label htmlFor="email" >Email:</Form.Label>
                            <Form.Control type="text" id="email" name="email" onChange={handleChange} />
                        </div>

                        <Form.Label htmlFor="password">Password:</Form.Label>
                        <Form.Control readOnly type="text" id="password" name="password" value={"Password will be sent to your email"} />

                        <Form.Label htmlFor="roles">Roles:</Form.Label>
                        <AsyncSelect
                            cacheOptions
                            defaultOptions
                            loadOptions={loadRoles}
                            isMulti={true}
                            onChange={handleRolesChange}
                        />
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Add Account
                    </Button>
                </Modal.Footer>
            </Modal >
        );
    };



    // Render the table only when there is data available
    const renderTable = () => {
        if (accountData.length === 0) {
            return <p>No data available.</p>; // Show a message when no data
        }
        return (
            <table className="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>Pasword</th>
                        <th>Role</th>
                        <th>Actions</th>
                        {/* Add more table headers for other account properties if needed */}
                    </tr>
                </thead>
                <tbody>
                    {accountData.map((account) => (
                        <tr key={account.email}>
                            <td>{account.email}</td>
                            <td type="pasword">{account.password}</td>
                            <td>
                                <ul>
                                    {account.role.map((r) => (
                                        <li key={r.name}>{r.name}</li>
                                    ))}
                                </ul>
                            </td>

                            <td>
                                <button
                                    className="btn btn-primary"
                                    onClick={() => handleUpdateClick({account})}>
                                    Update
                                </button>
                                <button className="btn btn-danger"
                                    onClick={() => handleDeleteClick({account})}>
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
            <div className="account-container">
                <h2>List of Account:</h2>
                {/* Render the table using the conditional function */}
                <button className="btn btn-primary" onClick={handleAddClick}>
                    Add New Account
                </button>
                {/* Search input fields */}
                {/* Search form */}
                <div>
                    <Form onSubmit={handleSearchClick} className="search-form">
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
                    <UpdateAccountModal
                        account={selectedRowData}
                        showModal={showModal}
                    />
                )}
                {/* Modal delete*/}
                {showDeleteModal && (
                    <DeleteAccountModal
                        account={selectedRowData}
                        showDeleteModal={showDeleteModal}
                    />
                )}
                {/* Modal add*/}
                {showAddModal && (
                    <AddAccountModal
                        showModal={showAddModal}
                    />
                )}
            </div>
        </div>
    );

}

export default Account;
