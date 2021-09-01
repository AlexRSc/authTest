import { NavLink } from 'react-router-dom'
import styled from 'styled-components/macro'

export default function Navbar(props) {
  return (
    <Wrapper {...props}>
      <NavLink exact to="/profile">
        Profile
      </NavLink>
      <NavLink to="/login">Login</NavLink>
      <NavLink to="/logout">Logout</NavLink>
      <NavLink to="/admin">Admin</NavLink>
    </Wrapper>
  )
}

const Wrapper = styled.nav`
  border-top: 1px solid var(--neutral-dark);
  width: 100%;
  padding: var(--size-m);
  display: flex;
  overflow-y: scroll;

  a {
    flex-grow: 1;
    margin: 0 var(--size-m);
    text-align: center;
    text-decoration: none;
    color: var(--neutral-dark);
  }

  a.active {
    color: var(--accent);
  }
`
