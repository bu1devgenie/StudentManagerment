const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/login','/checkAccessToken',
    createProxyMiddleware({
      target: 'http://localhost:9999',
      changeOrigin: true,
    })
  );
};