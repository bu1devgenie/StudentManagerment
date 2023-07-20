import React from 'react';
import ReactDOM from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'; // Import BrowserRouter as Router
import App from './App';
import Home from './Home';
import Teacher from './Teacher';

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <Router>
    <Routes>
      <Route path="/home" element={<Home />} />
      <Route path="/login" element={<App />} />
      <Route path="/teacher" element={<Teacher />} />

    </Routes>
  </Router>,
);
