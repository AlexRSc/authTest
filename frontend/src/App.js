import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import Login from './pages/Login'
import Profile from './pages/Profile'
import ChangePassword from './pages/ChangePassword'
import Logout from './pages/Logout'
import Admin from './pages/Admin'
import { useState } from 'react'
import { getToken } from './services/api-service'
import jwt from 'jsonwebtoken'
import ProtectedRoute from './auth/ProtectedRoute'

export default function App() {
  const [token, setToken] = useState()

  const claims = jwt.decode(token)

  const user = claims && {
    username: claims.sub,
    avatar: claims.avatar,
    role: claims.role,
  }

  const login = credentials => getToken(credentials).then(setToken)

  const logout = () => setToken()

  return (
    <Router>
      <Switch>
        <Route path="/login">
          <Login onLogin={login} token={token} user={user} />
        </Route>
        <ProtectedRoute user={user} exact path="/">
          <Profile user={user} />
        </ProtectedRoute>
        <ProtectedRoute user={user} path="/logout">
          <Logout onLogout={logout} user={user} />
        </ProtectedRoute>
        <ProtectedRoute user={user} path="/change-password">
          <ChangePassword user={user} />
        </ProtectedRoute>
        <ProtectedRoute adminOnly user={user} path="/admin">
          <Admin user={user} />
        </ProtectedRoute>
      </Switch>
    </Router>
  )
}
