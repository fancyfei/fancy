package com.fancy.common.util.model;

import java.util.Date;

/**
 * Sample Employee bean to demostrate simple export features
 * author: Leonid Vysochyn
 */
public class Employee {
    private String name;
    private int age;
    private Double payment;
    private Double bonus;
    private Date birthDate;
    private Employee superior;
    public Employee() {
		super();
	}

	public Employee(String name, int age, Double payment, Double bonus) {
        this.name = name;
        this.age = age;
        this.payment = payment;
        this.bonus = bonus;
    }

    public Employee(String name, int age, double payment, double bonus, Date birthDate) {
        this.name = name;
        this.age = age;
        this.payment = new Double(payment);
        this.bonus = new Double(bonus);
        this.birthDate = birthDate;
    }

    public Employee(String name, int age, double payment, double bonus) {
        this.name = name;
        this.age = age;
        this.payment = new Double(payment);
        this.bonus = new Double(bonus);
    }

    @Override
	public String toString() {
		return "Employee [name=" + name + ", age=" + age + ", payment=" + payment + ", bonus=" + bonus + ", birthDate=" + birthDate + ", superior=" + superior + "]\n";
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Employee getSuperior() {
        return superior;
    }

    public void setSuperior(Employee superior) {
        this.superior = superior;
    }
}
