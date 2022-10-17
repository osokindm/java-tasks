package ru.mail.polis.homework.oop.vet;

public class Pigeon extends Animal implements Pet, WildAnimal {

    String organizationName;

    public Pigeon(int legs) {
        super(legs);
    }

    @Override
    public String say() {
        return "curls-curls";
    }

    @Override
    public MoveType moveType() {
        return MoveType.FLY;
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
