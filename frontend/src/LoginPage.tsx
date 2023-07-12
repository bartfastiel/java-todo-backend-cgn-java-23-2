import {FormEvent, useState} from "react";
import axios from "axios";


export default function LoginPage() {

    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    function onLogin(event: FormEvent) {
        event.preventDefault()
        axios.post("/api/users/login", null, {auth: {username, password}})
            .then((response) => {
                console.log(response)
            })
    }

    return (
        <form onSubmit={onLogin}>
            <p>Login</p>
            <input value={username} onChange={event => setUsername(event.target.value)} placeholder="Username"/>
            <input value={password} onChange={event => setPassword(event.target.value)} placeholder="Password" type="password"/>
            <button>Login</button>
        </form>
    )
}
