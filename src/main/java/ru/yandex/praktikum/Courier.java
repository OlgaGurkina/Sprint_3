package ru.yandex.praktikum;

public class Courier {
    private String login;
    private String password;
    private String firstname;


    public Courier() {
    }

    public Courier(String login, String password, String firstname) {
        this.login = login;
        this.password = password;
        this.firstname = firstname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}


