import React from 'react';
import {Nav, NavDropdown} from 'react-bootstrap';
import {Link, Route, Routes, useNavigate} from 'react-router-dom'; // Import useNavigate
import 'bootstrap/dist/css/bootstrap.min.css';


function Navbar() {
    const navigate = useNavigate(); // Initialize useNavigate

    const handleSelect = (eventKey) => {
        // Use navigate to change the URL path when an item is clicked
        switch (eventKey) {
            case '1':
                navigate("/teacher");
                break;
            case '2':
                navigate("/student");
                break;
            case '3':
                navigate("/user");
                break;
            case '4':
                navigate("/account");
                break;
            case '5':
                navigate("/course");
                break;
            case '6':
                navigate("/schedule");
                break;
            case '7':
                navigate("/attendence");
                break;
            case '8.1':
                navigate("/profile");
                break;
            case '8.2':
                navigate("/account");
                break;
            case '8.3':
                navigate("/logout");
                break;
            default:
                break;
        }
    };

    return (
        <div>
            <Nav variant="pills" activeKey="0" onSelect={handleSelect}>
                <Nav.Item>
                    <Nav.Link eventKey="0">
                        Trường Làng
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="1">
                        Teacher
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="2">
                        Student
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="3">
                        User
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="4">
                        Account
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="5">
                        Course
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="6">
                        Schedule
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link eventKey="7">
                        Attendance
                    </Nav.Link>
                </Nav.Item>
                <NavDropdown title="Username" id="nav-dropdown">
                    <NavDropdown.Item eventKey="8.1">Profile</NavDropdown.Item>
                    <NavDropdown.Item eventKey="8.2">Account</NavDropdown.Item>
                    <NavDropdown.Divider />
                    <NavDropdown.Item eventKey="8.3">Logout</NavDropdown.Item>
                </NavDropdown>
            </Nav>

        </div>
    );
}

export default Navbar;
