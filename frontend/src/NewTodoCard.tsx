import {useState} from "react";
import axios from "axios";
import {Todo} from "./Todo.ts";

type Props = {
    onTodoItemChange: () => void,
}

export default function NewTodoCard(props: Props) {

    const [text, setText] = useState("");
    const [errorText, setErrorText] = useState<string>("")

    function changeText(event: React.ChangeEvent<HTMLInputElement>) {
        setText(event.target.value)

        if (event.target.value.length <= 5) {
            setErrorText("Eingabe ist zu kurz!")
        }
        else {
            setErrorText("")
        }
    }

    function saveTodo() {
        setText("")
        axios.post("/api/todo",
            {
                description: text,
                status: "OPEN",
            } as Todo)
            .then(props.onTodoItemChange)
    }

    return (
        <div className="todo-card new-todo">
            <p>{errorText}</p>
            <input type="text" value={text} onInput={changeText}/>
            <button onClick={saveTodo}>Save</button>
        </div>
    );
}
