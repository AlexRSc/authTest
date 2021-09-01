import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import TextField from '../components/TextField'
import Main from '../components/Main'
import { useState } from 'react'
import { Redirect } from 'react-router-dom'
import Loading from '../components/Loading'
import Error from '../components/Error'

const initialState = {
  username: '',
  password: '',
}

export default function Login({ token, onLogin }) {
  const [credentials, setCredentials] = useState(initialState)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState()

  const handleCredentialsChange = event =>
    setCredentials({ ...credentials, [event.target.name]: event.target.value })

  const handleSubmit = event => {
    event.preventDefault()
    setLoading(true)
    setError()
    onLogin(credentials).catch(error => {
      setError(error)
      setLoading(false)
    })
  }

  if (token) {
    return <Redirect to="/profile" />
  }

  return (
    <Page>
      <Header title="Login" />
      {loading && <Loading />}
      {!loading && (
        <Main as="form" onSubmit={handleSubmit}>
          <TextField
            title="Username"
            name="username"
            value={credentials.username}
            onChange={handleCredentialsChange}
          />
          <TextField
            title="Password"
            name="password"
            type="password"
            value={credentials.password}
            onChange={handleCredentialsChange}
          />
          <Button>login</Button>
        </Main>
      )}
      {error && <Error>{error.message}</Error>}
      <Navbar />
    </Page>
  )
}
