import {Navigate, Outlet} from "react-router-dom";


type Props = {
    user?: string
}
export default function ProtectedRoutes(props: Props) {

    const isAuthenticated = props.user !== undefined && props.user !== 'anonymousUser'

//             Zeigt alle Unter-Routen : Leitet auf /Login weiter
    return (
        isAuthenticated ? <Outlet /> : <Navigate to="/login" />
    )
}
