import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import TextField from '../components/TextField'
import ButtonGroup from '../components/ButtonGroup'
import Main from '../components/Main'
import {useAuth} from '../auth/AuthProvider'
import {useState} from "react";
import Error from "../components/Error";
import {Redirect} from "react-router-dom";


export default function ChangePassword() {
    const [password, setPassword] = useState()
    const [secPassword, setSecPassword] = useState()
    const {user, changePassword} = useAuth()

    const handleSubmit = (event) => {
        event.preventDefault()
        if (password === secPassword) {
            changePassword(password)
        }
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value)
    }
    const handleSecPasswordChange = (event) => {
        setSecPassword(event.target.value)
    }

    return (
        <Page>
            <Header title="Change Password"/>
            <Main as="form" onSubmit={handleSubmit}>
                <TextField title="New Password"
                           name="new-password"
                           type="password"
                           onChange={handlePasswordChange}/>
                <TextField
                    title="Retype Password"
                    name="retype-password"
                    type="password"
                    onChange={handleSecPasswordChange}
                />
                <ButtonGroup>
                    <Button secondary >Cancel</Button>
                    <Button>Save</Button>
                </ButtonGroup>
            </Main>
            {password!==secPassword&&<Error>Passwords are not equal</Error>}
            <Navbar user={user}/>
        </Page>
    )
}
