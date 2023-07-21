import React, {useState, useEffect} from 'react';
import ReactDOM from "react-dom/client";
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom'; // Import BrowserRouter as Router
import App from './App';
import Home from './Home';
import Teacher from './teacher/Teacher';
import Student from './Student';
import Cookies from 'js-cookie';
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles.css';



const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <Router>
    <Routes>
      <Route path="/home" element={<Home />} />
      <Route path="/login" element={<App />} />
      <Route path="/teacher" element={<Teacher />} />
      <Route path="/student" element={<Student />} />

    </Routes>
  </Router>,
);
