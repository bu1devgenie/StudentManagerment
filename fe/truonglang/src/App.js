import {useState, useEffect} from 'react';
import Cookies from 'js-cookie';
import Home from './Home';

function App() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Thêm state để lưu trạng thái đăng nhập

  useEffect(() => {
    // Kiểm tra có AccessToken trong cookie hay không
    const accessToken = Cookies.get('accessToken');
    if (accessToken !== undefined) {
      // Gọi server để kiểm tra access token
      fetch('/checkAccessToken', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': accessToken
        },
      })
        .then((response) => {
          if (response.status === 200) {
            setIsLoggedIn(true); // Đánh dấu là đã đăng nhập thành công
          } else {
            throw new Error('Hết phiên đăng nhập');
          }
        })
        .catch((error) => {
          alert(error.message);
          setIsLoggedIn(false);
        });
    }
  }, []);

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
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({email, password}),
    })
      .then((response) => {
        if (response.status === 403) {
          throw new Error('sai tk mk');
        }
        return response.json(); // Chuyển đổi phản hồi thành JSON
      })
      .then((data) => {
        // Lưu từng phần vào cookie
        Cookies.set('email', data.email);
        Cookies.set('ms', data.ms);
        Cookies.set('name', data.name);
        Cookies.set('avatar', data.avatar);
        Cookies.set('accessToken', data.accessToken);
        setIsLoggedIn(true); // Đánh dấu là đã đăng nhập thành công
      })
      .catch((error) => {
        alert(error.message);
      });
  };

  return (
    <div>
      {isLoggedIn ? (
        <Home />
      ) : (
        <div>
          <h2>Đăng nhập</h2>
          <form onSubmit={handleSubmit}>
            <label htmlFor="email">Tên đăng nhập:</label>
            <br />
            <input
              type="text"
              id="email"
              value={email}
              onChange={handleEmailChange}
            />
            <br />
            <label htmlFor="password">Mật khẩu:</label>
            <br />
            <input
              type="password"
              id="password"
              value={password}
              onChange={handlePasswordChange}
            />
            <br />
            <br />
            <input type="submit" value="Đăng nhập" />
          </form>
        </div>
      )}
    </div>
  );
}


export default App;

