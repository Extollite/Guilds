package pl.extollite.guilds.utils;

public class IdMeta {
    private int id;
    private int meta;

    public IdMeta(int id, int meta) {
        this.id = id;
        this.meta = meta;
    }

    public int getId() {
        return id;
    }

    public int getMeta() {
        return meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdMeta idMeta = (IdMeta) o;

        if (id != idMeta.id) return false;
        return meta == idMeta.meta;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + meta;
        return result;
    }

    @Override
    public String toString() {
        return id+":"+meta;
    }
}

