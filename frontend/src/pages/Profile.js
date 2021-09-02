import styled from 'styled-components/macro'

import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import { Link } from 'react-router-dom'
import Main from '../components/Main'
import Avatar from '../components/Avatar'
import Badge from '../components/Badge'
import { useAuth } from '../auth/AuthProvider'

export default function Profile() {
  const { user } = useAuth()

  return (
    <Page>
      <Header title={user.username} />
      <Main>
        <Avatar src={user.avatar} alt="" />
        <Badge>{user.role}</Badge>
        <Button as={LinkStyled} to="/change-password">
          Change Password
        </Button>
      </Main>
      <Navbar user={user} />
    </Page>
  )
}

const LinkStyled = styled(Link)`
  text-decoration: none;
`
