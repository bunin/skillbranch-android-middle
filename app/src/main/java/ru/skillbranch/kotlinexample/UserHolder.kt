package ru.skillbranch.kotlinexample

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        val u = User.makeUser(
            fullName = fullName,
            email = email,
            password = password
        )
        if (map.containsKey(u.login)) {
            throw IllegalArgumentException("A user with this email already exists")
        }
        map[u.login] = u
        return u
    }

    fun registerUserByPhone(
        fullName: String,
        rawPhone: String
    ): User {
        val u = User.makeUser(
            fullName = fullName,
            phone = rawPhone
        )
        if (!validatePhone(rawPhone)) {
            throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        }
        if (map.containsKey(u.login)) {
            throw IllegalArgumentException("A user with this phone already exists")
        }
        map[u.login] = u
        return u
    }

    fun loginUser(login: String, password: String): String? {
        val u = if (login.contains('@')) User.makeUser("N A", login, login) else User.makeUser(
            "N A",
            phone = login
        )
        return map[u.login]?.run {
            return if (checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(phone: String) {
        val u = User.makeUser("N A", phone = phone)
        map[u.login]!!.resetCode()
    }

    private fun validatePhone(v: String): Boolean {
        var numbers = 0
        var plus = 0
        for (c in v) {
            if (c == '+') {
                plus++
            }
            if (c.isDigit()) {
                numbers++
            }
        }
        return plus < 2 && numbers == 11
    }

    fun reset() {
        map.clear()
    }
}