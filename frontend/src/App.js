import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import Login from './pages/Login'
import Profile from './pages/Profile'
import ChangePassword from './pages/ChangePassword'
import Logout from './pages/Logout'
import Admin from './pages/Admin'
import Welcome from './pages/Welcome'
import { useState } from 'react'
import { getToken } from './services/api-service'
import jwt from 'jsonwebtoken'

export default function App() {
  const [token, setToken] = useState()

  const claims = jwt.decode(token)

  const user = claims && {
    username: claims.sub,
    avatar: claims.avatar,
    role: claims.role,
  }

  const login = credentials => getToken(credentials).then(setToken)

  return (
    <Router>
      <Switch>
        <Route exact path="/" component={Welcome} />
        <Route exact path="/profile">
          <Profile user={user} />
        </Route>
        <Route path="/login">
          <Login onLogin={login} token={token} />
        </Route>
        <Route path="/logout" component={Logout} />
        <Route path="/admin" component={Admin} />
        <Route path="/change-password" component={ChangePassword} />
      </Switch>
    </Router>
  )
}
