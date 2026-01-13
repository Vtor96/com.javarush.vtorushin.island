package entity.island;

public class Island {
    private int width, height;
    private Location[][] locations;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        this.locations = new Location[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                locations[y][x] = new Location(x, y, this);
    }

    public Location getLocation(int x, int y) {
        if (x >= 0 && y >= 0 && y < height && x < width)
            return locations[y][x];
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
