import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'; // Import BrowserRouter as Router
import App from './App';
import Home from './Home';
import Teacher from './Teacher';

ReactDOM.render(
  <Router>
    <Routes>
      <Route path="/home" element={<Home />} />
      <Route path="/login" element={<App />} />
      <Route path="/teacher" element={<Teacher />} />

    </Routes>
  </Router>,
  document.getElementById('root')
);
