import React from 'react';
import {Routes, Route} from 'react-router-dom';
import Navbar from './Navbar'; // Import the Navbar component
import 'bootstrap/dist/css/bootstrap.min.css';


const teacherData = [
    // The provided JSON data of teachers (Replace this with the actual data from your backend)
    {
        "id": 1,
        "msgv": "FPT-Teacher-00000",
        "name": "Phạm Thị Minh Thuận",
        "address": "3476 Old Front Lane, Penobscot Bldg, Annapolis, Maryland, 76341",
        "dob": "1966-12-16",
        "avatar": "https://image.vtc.vn/resize/th/upload/2021/04/07/ek6qycqxiaiyiun-11052098.jpg",
        "course": [
            {
                "id": 1,
                "name": "Architecture",
                "courseSemester": 1,
                "totalSlot": 34
            },
            {
                "id": 5,
                "name": "Astronomy",
                "courseSemester": 5,
                "totalSlot": 33
            },
            {
                "id": 24,
                "name": "Special Education",
                "courseSemester": 4,
                "totalSlot": 34
            },
            {
                "id": 25,
                "name": "African American Studies",
                "courseSemester": 5,
                "totalSlot": 31
            }
        ],
        "email": "fifakillmen@gmail.com",
        "gender": "MALE"
    },
    // Add more teacher objects if needed
];

function Teacher() {
    return (
        <div>
            <Navbar />
            <div className="container mt-4">
                <h2>List of Teachers:</h2>
                <table className="table table-bordered table-striped" style={
                    {width: '1400px'}}>
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
                                    <button className="btn btn-primary">Update</button>
                                    <button className="btn btn-danger">Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default Teacher;
