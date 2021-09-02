import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import Login from './pages/Login'
import Profile from './pages/Profile'
import ChangePassword from './pages/ChangePassword'
import Logout from './pages/Logout'
import Admin from './pages/Admin'
import ProtectedRoute from './auth/ProtectedRoute'
import AuthProvider from './auth/AuthProvider'

export default function App() {
  return (
    <AuthProvider>
      <Router>
        <Switch>
          <Route path="/login" component={Login} />
          <ProtectedRoute exact path="/" component={Profile} />
          <ProtectedRoute path="/logout" component={Logout} />
          <ProtectedRoute path="/change-password" component={ChangePassword} />
          <ProtectedRoute adminOnly path="/admin" component={Admin} />
        </Switch>
      </Router>
    </AuthProvider>
  )
}
