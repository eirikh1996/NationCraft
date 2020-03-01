package io.github.eirikh1996.nationcraft.api.objects;

public class NCVector {
    private double x, y, z;

    public NCVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getBlockX() {
        return (int) x;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getBlockY() {
        return (int) y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return z;
    }

    public int getBlockZ() {
        return (int) z;
    }

    public NCVector subtract(NCVector sLoc) {
        return new NCVector(x - sLoc.x, y - sLoc.y, z - sLoc.z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

}
