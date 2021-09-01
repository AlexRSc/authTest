import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import TextField from '../components/TextField'
import ButtonGroup from '../components/ButtonGroup'
import Main from '../components/Main'

export default function ChangePassword() {
  return (
    <Page>
      <Header title="Change Password" />
      <Main as="form">
        <TextField title="New Password" name="new-password" type="password" />
        <TextField
          title="Retype Password"
          name="retype-password"
          type="password"
        />
        <ButtonGroup>
          <Button secondary>Cancel</Button>
          <Button>Save</Button>
        </ButtonGroup>
      </Main>
      <Navbar />
    </Page>
  )
}
