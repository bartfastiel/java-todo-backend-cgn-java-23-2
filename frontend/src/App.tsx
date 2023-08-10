import {Todo} from "./Todo.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import TodoColumn from "./TodoColumn.tsx";
import {allPossibleTodos} from "./TodoStatus.ts";
import LoginPage from "./LoginPage";
import {Route, Routes, useNavigate} from "react-router-dom";
import ProtectedRoutes from "./ProtectedRoutes";

function App() {

    const [user, setUser] = useState<string>()
    const [todos, setTodos] = useState<Todo[]>()

    const navigate = useNavigate()

    function fetchTodos() {
        if (user !== undefined && user !== "anonymousUser") {
            axios.get("/api/todo", {headers: {
                Authorization: "Bearer " + user.token
                }})
                .then(response => {
                    setTodos(response.data)
                })
        }
    }

    function login(username: string, password: string) {
        axios.post("/api/users/login", null, {auth: {username, password}})
            .then((response) => {
                setUser(response.data)
                navigate("/")
            })
    }

    function me() {
        axios.get("/api/users/me2")
            .then(response => {
                setUser(response.data)
            })
    }

    function logout() {
        axios.post("/api/users/logout")
            .then(() => {
                me()
            })
    }

    useEffect(() => {
        fetchTodos()
        me()
    }, [user])

    return (
        <div>
            <div>
                <h1>TODOs</h1>
                <p>{user}</p>
                {
                    user === undefined || user !== "anonymousUser"
                        ? <button onClick={() => logout()}>Logout</button>
                        : <button onClick={() => navigate("/login")}>Login</button>
                }

            </div>

            <Routes>
                <Route element={<ProtectedRoutes user={user}/>}>

                    <Route path="/" element={<div className="page">
                        {
                            !todos
                                ? <p>Lade...</p>
                                : allPossibleTodos.map(status => {
                                    const filteredTodos = todos.filter(todo => todo.status === status)
                                    return <TodoColumn
                                        status={status}
                                        todos={filteredTodos}
                                        onTodoItemChange={fetchTodos}
                                        key={status}
                                    />
                                })
                        }
                    </div>}/>
                    <Route path="home" element={<p>Home</p>}/>
                </Route>


                <Route path="/login" element={<LoginPage onLogin={login}/>}/>
            </Routes>

        </div>
    )
}

export default App
