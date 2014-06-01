package chess;

import java.util.*;

/**
 * User: Alpen Ditrix
 * Date: 17.05.2014
 * Time: 7:49
 */
public class ChessGame {

    public static final int WHITE = 0;
    public static final int BLACK = 1;

    public static final  int        PAWN       = 1;
    public static final  int        BISHOP     = 2;
    public static final  int        KNIGHT     = 3;
    public static final  int        ROOK       = 4;
    public static final  int        QUEEN      = 5;
    public static final  int        KING       = 6;
    private static final short      BOARD_SIZE = 8;
    private final       int[][]    board      = new int[BOARD_SIZE][BOARD_SIZE];
    private final        AttackArea aa         = new AttackArea();
    private final        StepArea   sa         = new StepArea();
    private final        MakesCheck mc         = new MakesCheck();
    private final StringBuilder history = new StringBuilder("\nHistory:\n");
    private boolean whiteTurn = true;
    private int turnCount;
    private int ID1, ID2;
    private TurnInfo lastTurnInfo;

    public ChessGame(int whiteId, int blackId) {
        ID1 = whiteId;
        ID2 = blackId;
        setUp(board);
    }

    public static final class Cell {
        private static final char[]                  l            = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        private static final Map<Character, Integer> letter2index = new HashMap<Character, Integer>() {
            {
                put('a', 0);
                put('b', 1);
                put('c', 2);
                put('d', 3);
                put('e', 4);
                put('f', 5);
                put('g', 6);
                put('h', 7);
            }
        };
        final int i;
        final int j;

        public Cell(String pretty) {
            if (pretty.length() != 2) {
                throw new RuntimeException("Wrong cell: " + pretty);
            }
            i = Character.getNumericValue(pretty.charAt(1));
            j = Character.getNumericValue(letter2index.get(pretty.charAt(0)));
        }

        private Cell(int x, int y) {
            this.i = x;
            this.j = y;
        }

        public String toString() {
            return '[' + i + ", " + j + ']';
        }

        public String toStringPretty() {
            return l[j] + "" + i;
        }

        public boolean wrong() {
            return 0 > i || i >= BOARD_SIZE || 0 > j || j >= BOARD_SIZE;
        }

        public boolean correct() {
            return 0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE;
        }

        public Cell q() {
            return new Cell(i - 1, j - 1);
        }

        public Cell w() {
            return new Cell(i - 1, j);
        }

        public Cell e() {
            return new Cell(i - 1, j + 1);
        }

        public Cell a() {
            return new Cell(i, j - 1);
        }

        public Cell d() {
            return new Cell(i, j + 1);
        }

        public Cell z() {
            return new Cell(i + 1, j - 1);
        }

        public Cell x() {
            return new Cell(i + 1, j);
        }

        public Cell c() {
            return new Cell(i + 1, j + 1);
        }

        public Cell q(int i) {
            return new Cell(this.i - i, j - i);
        }

        public Cell w(int i) {
            return new Cell(this.i - i, j);
        }

        public Cell e(int i) {
            return new Cell(this.i - i, j + i);
        }

        public Cell a(int i) {
            return new Cell(this.i, j - i);
        }

        public Cell d(int i) {
            return new Cell(this.i, j + i);
        }

        public Cell z(int i) {
            return new Cell(this.i + i, j - i);
        }

        public Cell x(int i) {
            return new Cell(this.i + i, j);
        }

        public Cell c(int i) {
            return new Cell(this.i + i, j + i);
        }

        public boolean equals(Object o) {
            return o instanceof Cell && i == ((Cell) o).i && j == ((Cell) o).j;
        }
    }

    private static final class ToStringer {
        private static final char[] SS = {'_', 'p', 'b', 'k', 'r', 'q', 'g', 'X', 'X', 'X', 'X', 'P', 'B', 'K', 'R',
                'Q', 'G'};
        private static final char[] S  = {'＿', '♙', '♗', '♘', '♖', '♕', '♔', 'X', 'X', 'X', 'X', '♟', '♝', '♞', '♜',
                '♛', '♚'};
        private static final char   I  = '|';

        public static String parseWithArea(int[][] board, List<Cell> area) {
            int[][] newBoard = new int[board.length][];
            for (int i = 0; i < board.length; i++) {
                newBoard[i] = Arrays.copyOf(board[i], board[i].length);
            }
            for (Cell c : area) {
                newBoard[c.i][c.j] = 10;
            }
            return parse(newBoard);
        }

        private static String parse(int[][] board) {
            return parse(board, true);
        }

        private static String parse(int[][] board, boolean simple) {
            StringBuilder sb = new StringBuilder();
            for (int[] line : board) {
                sb.append(I);
                for (int fig : line) {
                    sb.append((simple ? SS : S)[fig]);
                    sb.append(I);
                }
                sb.append('\n');
            }
            return sb.toString();
        }

        public static String toStringPretty(ChessGame game) {
            return parse(game.board, false);
        }

        public static String showCell(ChessGame game, final Cell cell) {
            return parseWithArea(game.board, new ArrayList<Cell>() {{add(cell);}});
        }

        public static String showArea(ChessGame game, List<Cell> area) {
            return parseWithArea(game.board, area);
        }
    }

    private final class MakesCheck {
        private boolean checkCell(Cell a, int kingColor) {
            int fig = getFigure(a);
            return fig % 10 == KING && fig / 10 == kingColor;
        }

        public boolean pawn(Cell c, int kingColor) {
            for (Cell a : aa.pawn(c)) {
                if (checkCell(a, kingColor)) {
                    return true;
                }
            }
            return false;
        }

        public boolean bishop(Cell c, int kingColor) {
            Cell bishRunner = c.e();
            while (isEmpty(bishRunner)) {
                bishRunner = bishRunner.e();
            }
            if (checkCell(bishRunner, kingColor)) { return true; }
            bishRunner = c.c();
            while (isEmpty(bishRunner)) {
                bishRunner = bishRunner.c();
            }
            if (checkCell(bishRunner, kingColor)) { return true; }
            bishRunner = c.z();
            while (isEmpty(bishRunner)) {
                bishRunner = bishRunner.z();
            }
            if (checkCell(bishRunner, kingColor)) { return true; }
            bishRunner = c.q();
            while (isEmpty(bishRunner)) {
                bishRunner = bishRunner.q();
            }
            return checkCell(bishRunner, kingColor);
        }

        public boolean rook(Cell cell, int kingColor) {
            Cell rookRunner = cell.d();
            while (isEmpty(rookRunner)) {
                rookRunner = rookRunner.d();
            }
            if (checkCell(rookRunner, kingColor)) { return true; }
            rookRunner = cell.x();
            while (isEmpty(rookRunner)) {
                rookRunner = rookRunner.x();
            }
            if (checkCell(rookRunner, kingColor)) { return true; }
            rookRunner = cell.a();
            while (isEmpty(rookRunner)) {
                rookRunner = rookRunner.a();
            }
            if (checkCell(rookRunner, kingColor)) { return true; }
            rookRunner = cell.w();
            while (isEmpty(rookRunner)) {
                rookRunner = rookRunner.w();
            }
            return checkCell(rookRunner, kingColor);
        }

        public boolean queen(Cell c, int kingColor) {
            return bishop(c, kingColor) || rook(c, kingColor);
        }

        public boolean knight(Cell c, int kingColor) {
            for (Cell a : aa.knight(c)) {
                if (checkCell(a, kingColor)) { return true; }
            }
            return false;
        }
    }

    private final class AttackArea {
        public List<Cell> bishop(Cell of) {
            List<Cell> list = new ArrayList<Cell>();
            Cell bishRunner = of.e();
            while (!bishRunner.wrong()) {
                list.add(bishRunner);
                bishRunner = bishRunner.e();
            }
            bishRunner = of.c();
            while (!bishRunner.wrong()) {
                list.add(bishRunner);
                bishRunner = bishRunner.c();
            }
            bishRunner = of.z();
            while (!bishRunner.wrong()) {
                list.add(bishRunner);
                bishRunner = bishRunner.z();
            }
            bishRunner = of.q();
            while (!bishRunner.wrong()) {
                list.add(bishRunner);
                bishRunner = bishRunner.q();
            }
            return list;
        }

        public List<Cell> rook(Cell of) {
            List<Cell> list = new ArrayList<Cell>();
            Cell rookRunner = of.d();
            while (!rookRunner.wrong()) {
                list.add(rookRunner);
                rookRunner = rookRunner.d();
            }
            rookRunner = of.x();
            while (!rookRunner.wrong()) {
                list.add(rookRunner);
                rookRunner = rookRunner.x();
            }
            rookRunner = of.a();
            while (!rookRunner.wrong()) {
                list.add(rookRunner);
                rookRunner = rookRunner.a();
            }
            rookRunner = of.w();
            while (!rookRunner.wrong()) {
                list.add(rookRunner);
                rookRunner = rookRunner.w();
            }
            return list;
        }

        public List<Cell> queen(Cell of) {
            List<Cell> bish = bishop(of);
            bish.addAll(rook(of));
            return bish;
        }

        public List<Cell> king(Cell of) {
            List<Cell> list = new ArrayList<Cell>();
            list.add(of.q());
            list.add(of.w());
            list.add(of.e());
            list.add(of.a());
            list.add(of.d());
            list.add(of.z());
            list.add(of.x());
            list.add(of.c());
            removeWrongCells(list);
            return list;
        }

        public List<Cell> knight(Cell of) {
            List<Cell> list = new ArrayList<Cell>();
            list.add(of.w(2).a());
            list.add(of.w(2).d());

            list.add(of.d(2).w());
            list.add(of.d(2).x());

            list.add(of.x(2).d());
            list.add(of.x(2).a());

            list.add(of.a(2).w());
            list.add(of.a(2).x());

            removeWrongCells(list);
            return list;
        }

        public List<Cell> pawn(Cell of) {
            List<Cell> list = new ArrayList<Cell>();
            int fig = getFigure(of);
            if (isWhite(fig)) {
                Cell l = of.q();
                if (l.correct()) { list.add(l); }
                Cell r = of.e();
                if (r.correct()) { list.add(r); }
            } else {
                Cell l = of.z();
                if (l.correct()) {list.add(l);}
                Cell r = of.c();
                if (r.correct()) {list.add(r);}
            }
            return list;
        }
    }

    private final class StepArea {
        public List<Cell> pawn(Cell cell) {
            List<Cell> list = aa.pawn(cell);
            int fig = getFigure(cell);
            if (isWhite(fig)) {
                Cell l = cell.q();
                Cell r = cell.e();
                if (getFigure(l) != 11 && getFigure(r) != 11) { list.add(cell.w()); }
            } else {
                Cell l = cell.z();
                Cell r = cell.c();
                if (getFigure(l) != 11 && getFigure(r) != 11) {list.add(cell.x());}
            }
            return list;
        }

        public List<Cell> bishop(Cell cell) {
            List<Cell> list = new ArrayList<Cell>();
            Cell bishRunner = cell.e();
            while (isEmpty(bishRunner)) {
                list.add(bishRunner);
                bishRunner = bishRunner.e();
            }
            list.add(bishRunner);
            bishRunner = cell.c();
            while (isEmpty(bishRunner)) {
                list.add(bishRunner);
                bishRunner = bishRunner.c();
            }
            list.add(bishRunner);
            bishRunner = cell.z();
            while (isEmpty(bishRunner)) {
                list.add(bishRunner);
                bishRunner = bishRunner.z();
            }
            list.add(bishRunner);
            bishRunner = cell.q();
            while (isEmpty(bishRunner)) {
                list.add(bishRunner);
                bishRunner = bishRunner.q();
            }
            list.add(bishRunner);
            return list;
        }

        public List<Cell> knight(Cell cell) {return aa.knight(cell);}

        public List<Cell> rook(Cell cell) {
            List<Cell> list = new ArrayList<Cell>();
            Cell rookRunner = cell.d();
            while (isEmpty(rookRunner)) {
                list.add(rookRunner);
                rookRunner = rookRunner.d();
            }
            list.add(rookRunner);
            rookRunner = cell.x();
            while (isEmpty(rookRunner)) {
                list.add(rookRunner);
                rookRunner = rookRunner.x();
            }
            list.add(rookRunner);
            rookRunner = cell.a();
            while (isEmpty(rookRunner)) {
                list.add(rookRunner);
                rookRunner = rookRunner.a();
            }
            list.add(rookRunner);
            rookRunner = cell.w();
            while (isEmpty(rookRunner)) {
                list.add(rookRunner);
                rookRunner = rookRunner.w();
            }
            list.add(rookRunner);
            return list;
        }

        public List<Cell> queen(Cell cell) {
            List<Cell> bish = bishop(cell);
            bish.addAll(rook(cell));
            return bish;
        }

        public List<Cell> king(Cell cell) {return aa.king(cell);}
    }

    private static void setUp(int[][] board) {
        board[0][0] = ROOK + 10;
        board[0][1] = KNIGHT + 10;
        board[0][2] = BISHOP + 10;
        board[0][3] = QUEEN + 10;
        board[0][4] = KING + 10;
        board[0][5] = BISHOP + 10;
        board[0][6] = KNIGHT + 10;
        board[0][7] = ROOK + 10;
        final int BLACK_PAWN = PAWN + 10;
        for (int j = 0; j < BOARD_SIZE; j++) {
            board[1][j] = BLACK_PAWN;
        }
        for (int j = 0; j < BOARD_SIZE; j++) {
            board[6][j] = PAWN;
        }
        board[7][0] = ROOK;
        board[7][1] = KNIGHT;
        board[7][2] = BISHOP;
        board[7][3] = QUEEN;
        board[7][4] = KING;
        board[7][5] = BISHOP;
        board[7][6] = KNIGHT;
        board[7][7] = ROOK;
    }

    public String getPrettyField() {
        return ToStringer.toStringPretty(this);
    }

    private boolean isWhite(int fig) { return fig < 10; }

    private int getFigure(Cell cell) {
        return board[cell.i][cell.j];
    }

    private void removeWrongCells(List<Cell> list) {
        ListIterator<Cell> iter = list.listIterator();
        while (iter.hasNext()) {
            Cell c = iter.next();
            if (!c.wrong()) {} else { iter.remove(); }
        }
    }

    private List<Cell> getStepArea(Cell of) {
        int fig = getFigure(of);
        switch (fig % 10) {
            case PAWN:
                return sa.pawn(of);
            case KNIGHT:
                return sa.knight(of);
            case QUEEN:
                return sa.queen(of);
            case BISHOP:
                return sa.bishop(of);
            case ROOK:
                return sa.rook(of);
            case KING:
                return sa.king(of);
            default:
                return new ArrayList<Cell>();
        }
    }

    private List<Cell> getAttackArea(Cell of) {
        int fig = getFigure(of);
        switch (fig % 10) {
            case PAWN:
                return aa.pawn(of);
            case KNIGHT:
                return aa.knight(of);
            case QUEEN:
                return aa.queen(of);
            case BISHOP:
                return aa.bishop(of);
            case ROOK:
                return aa.rook(of);
            case KING:
                return aa.king(of);
            default:
                return new ArrayList<Cell>();
        }
    }

    void endOfTurn() {
        whiteTurn = !whiteTurn;
        turnCount++;
        history.append('\n');
        //check check
    }

    public String toString() {
        return ToStringer.parse(board);
    }

    public TurnInfo makeTurn(Cell from, Cell to) {
        String error = null;
        String log;
        if (from.wrong()) {
            error = "Wrong turn: Wrong departure cell: " + from.toString();
        }
        if (to.wrong()) {
            error = "Wrong turn: Wrong arrival cell: " + to.toString();
        }

        if (isEmpty(from)) {
            error = "Wrong turn: Departure cell is empty";
        }
        int fig = getFigure(from);
        if (isWhite(fig) ^ whiteTurn) {
            error = "Wrong turn: You can't act with enemy chessman";
        }
        if (!getStepArea(from).contains(to)) {
            error = "Wrong turn: There's no path from " + from.toStringPretty() + " to " + to.toStringPretty();
        }
        if (isEmpty(to)) {
            putFigureAt(fig, to);
            putFigureAt(0, from);
            if (isCheck(true)) {
                putFigureAt(fig, from);
                putFigureAt(0, to);
                error = "Wrong turn: You can't make check to yourself";
            }
            log = String.format("%s moved: %s-%s", ToStringer.S[fig], from.toStringPretty(),
                                   to.toStringPretty())
                           .trim();
            history.append('[').append(turnCount).append(']').append(' ');
            history.append(ToStringer.S[fig]).append(from.toStringPretty()).append('-').append(to.toStringPretty());
        } else {
            int def = getFigure(to);
            if (similarColorFigures(from, to)) {
                error = "Wrong turn: Similar color figures at cells " + from.toStringPretty() + " and " +
                        to.toStringPretty();
            }
            putFigureAt(fig, to);
            putFigureAt(0, from);
            if (isCheck(true)) {
                putFigureAt(fig, from);
                putFigureAt(def, to);
                error = "Wrong turn: You can't make check to yourself";
            }
            log = String.format("%s ate %s %s:%s", ToStringer.S[fig], ToStringer.S[def], from.toStringPretty(), to
                    .toStringPretty());
            history.append('[').append(turnCount).append(']').append(' ');
            history.append(ToStringer.S[fig])
                   .append(from.toStringPretty())
                   .append(':')
                   .append(ToStringer.S[def])
                   .append(to.toStringPretty());
        }
        lastTurnInfo = new TurnInfo(log, isCheck(false), error);
        return lastTurnInfo;
    }

    private void putFigureAt(int fig, Cell at) {
        board[at.i][at.j] = fig;
    }

    private boolean isEmpty(Cell from) {
        return getFigure(from) % 10 == 0;
    }

    private boolean similarColorFigures(Cell c1, Cell c2) {
        return getFigure(c1) / 10 == getFigure(c2) / 10;
    }

    private boolean isCheck(boolean self) {
        int attackerColor = whiteTurn ^ self ? 0 : 1;
        int defenderColor = whiteTurn ^ self ? 1 : 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell c = new Cell(i, j);
                if (!isEmpty(c) && getFigure(c) / 10 == attackerColor) {
                    boolean tmpRes = false;
                    switch (getFigure(c) % 10) {
                        case PAWN:
                            tmpRes = mc.pawn(c, defenderColor);
                            break;
                        case KNIGHT:
                            tmpRes = mc.knight(c, defenderColor);
                            break;
                        case QUEEN:
                            tmpRes = mc.queen(c, defenderColor);
                            break;
                        case BISHOP:
                            tmpRes = mc.bishop(c, defenderColor);
                            break;
                        case ROOK:
                            tmpRes = mc.rook(c, defenderColor);
                            break;
                    }
                    if (tmpRes) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public int getActorId() {
        return whiteTurn?ID1:ID2;
    }

    public String getColor(int ID) {
        return ID == ID1 ? "White" : "Black";
    }

    public String getHistory() {
        return history.toString();
    }

    public TurnInfo getLastTurnInfo() {
        return lastTurnInfo;
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame(0, 1);
        System.out.println(ToStringer.toStringPretty(game));
    }
}
