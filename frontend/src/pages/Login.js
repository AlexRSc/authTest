import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import TextField from '../components/TextField'
import Main from '../components/Main'

export default function Login() {
  return (
    <Page>
      <Header title="Login" />
      <Main as="form">
        <TextField title="Username" name="username" />
        <TextField title="Password" name="password" type="password" />
        <Button>login</Button>
      </Main>
      <Navbar />
    </Page>
  )
}
