import axios from "axios";

class UserService {
    const BASE_URL = "http://localhost:8080"; 
    static async login(email, password) {
        try {
            const response = await axios.post(`${BASE_URL}/auth/login`, { email, password });
            return response.data;
        } catch (error) {
            throw error;
        }
    }
}

export default UserService;
