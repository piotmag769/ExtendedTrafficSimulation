public class Point {
    private static final int maxMaxSpeed = 7;
    public static Integer[] types = {0, 1, 2, 3, 5};
    private int type;
    private Point nextRight;
    private Point prevRight;
    private Point nextLeft;
    private Point prevLeft;
    private boolean moved;
    private int speed;
    private int maxSpeed;


    public void move() {
        if (!this.moved && this.type != 0 && this.type != 5)
        {
            outrunReturnMove();
        }
    }

    public void outrunReturnMove()
    {
        boolean D0minus = calculateDistance(true, false, true);
        boolean D1minus_return = calculateDistance(false, false, true);
        boolean D1minus_outrun = calculateDistance(false, false, false);
        boolean D1plus = calculateDistance(false, true, true);

        // if there is no car on the right (also detects if we are on the left), speed > 0 and other conditions - return
        if (this.prevRight.nextRight.type == 0 && D0minus && D1minus_return && D1plus)
        {
            Point current = this.nextRight.prevRight;

            current.moved = true;
            current.type = this.type;
            current.speed = this.speed;
            current.maxSpeed = this.maxSpeed;
            this.moved = true;
            this.type = 0;
            this.speed = 0;
        }
        // if there is no car on the left (also detects if we are on the right), speed > 0 and other conditions - outrun
        else if(this.prevLeft.nextLeft.type == 0 && speed < maxSpeed && D0minus && D1minus_outrun && D1plus)
        {
            Point current = this.nextLeft.prevLeft;

            current.moved = true;
            current.type = this.type;
            current.speed = this.speed + 1;
            current.maxSpeed = this.maxSpeed;
            this.moved = true;
            this.type = 0;
            this.speed = 0;
        }
        else // move regularly
        {
            if (speed < maxSpeed)
                speed++;

            Point current = this;


            if (this.nextRight.prevRight == this) // if we are on the right
                for (int i = 0; i < speed; i++) {
                    if (current.nextRight.type != 0) {
                        speed = i;
                        break;
                    }
                    current = current.nextRight;
                }
            else // if we are on the left
                for (int i = 0; i < speed; i++) {
                    if (current.nextLeft.type != 0) {
                        speed = i;
                        break;
                    }
                    current = current.nextLeft;
                }

            int temp = type;
            this.type = 0;
            this.moved = true;
            current.moved = true;
            current.type = temp;
            current.speed = this.speed;
            current.maxSpeed = this.maxSpeed;
            this.speed = 0;
        }
    }

    public void clicked() {
        this.type = 0;
    }

    public void clear() {
        this.type = 0;
    }

    public void setNextRight(Point nextRight) {
        this.nextRight = nextRight;
    }

    public void setPrevRight(Point prevRight) {
        this.prevRight = prevRight;
    }

    public void setNextLeft(Point nextLeft) {
        this.nextLeft = nextLeft;
    }

    public void setPrevLeft(Point prevLeft) {
        this.prevLeft = prevLeft;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        this.speed = switch(type)
        {
            case 0, 5 -> 0;
            case 1 -> 3;
            case 2 -> 5;
            case 3 -> 7;
            default -> throw new IllegalStateException("Unexpected type value: " + type);
        };
        this.maxSpeed = speed;
    }

    private boolean calculateDistance(boolean ifRight, boolean ifForward, boolean ifReturn)
    {
        if (ifRight)
        {
            Point current = this.nextRight.prevRight;

            if(ifForward)
                for (int i = 1; i < maxMaxSpeed + 1; i++)
                {
                    if (current.nextRight.type != 0)
                        return i >= speed;

                    current = current.nextRight;
                }
            else //ifBackward
                for (int i = 1; i < maxMaxSpeed + 1; i++)
                {
                    if (current.prevRight.type != 0)
                        return i >= current.prevRight.maxSpeed;

                    current = current.prevRight;
                }
        }
        else // ifLeft
        {
            Point current = this.nextLeft.prevLeft;

            if(ifForward)
                for (int i = 1; i < maxMaxSpeed + 1; i++)
                {
                    if (current.nextLeft.type != 0)
                        return i >= speed;

                    current = current.nextLeft;
                }
            else //ifBackward
                for (int i = 1; i < maxMaxSpeed + 1; i++)
                {
                    if (current.prevLeft.type != 0)
                        if (ifReturn)
                            return i <= current.prevLeft.maxSpeed;
                        else
                            return i >= current.prevLeft.maxSpeed;

                    current = current.prevLeft;
                }
        }

        boolean res = ifForward || ifRight || !ifReturn;
        return res;
    }
}
