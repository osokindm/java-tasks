package ru.mail.polis.homework.oop.vet;

public class Shark extends Animal implements WildAnimal {

    String organizationName;

    public Shark(int legs) {
        super(legs);
    }

    @Override
    public String say() {
        return "Clack Clack";
    }

    @Override
    public MoveType moveType() {
        return MoveType.SWIM;
    }

    @Override
    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
