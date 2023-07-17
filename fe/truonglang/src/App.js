import { useState } from 'react';
import Cookies from 'js-cookie';
import { useHistory } from 'react-router-dom';

function App() {
  
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const history = useHistory();

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    // Gửi yêu cầu đăng nhập đến server
    fetch('/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    })
      .then((response) => response.text())
      .then((data) => {
        setMessage(data);
        // Lưu dữ liệu vào cookie
        Cookies.set('data', data);

        // Chuyển hướng đến trang home
        history.push('/home');
      })
      .catch((error) => console.log(error));
  };

  return (
    <div>
      <h2>Đăng nhập</h2>
      <form onSubmit={handleSubmit}>
        <label htmlFor="email">Tên đăng nhập:</label>
        <br />
        <input type="text" id="email" value={email} onChange={handleEmailChange} />
        <br />
        <label htmlFor="password">Mật khẩu:</label>
        <br />
        <input type="password" id="password" value={password} onChange={handlePasswordChange} />
        <br />
        <br />
        <input type="submit" value="Đăng nhập" />
      </form>
      <p>{message}</p>
    </div>
  );
}

export default App;
