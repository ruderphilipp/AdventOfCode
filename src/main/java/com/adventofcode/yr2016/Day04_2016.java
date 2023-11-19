package com.adventofcode.yr2016;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.adventofcode.yr2016.Day04_2016.getLetterCount;

public abstract class Day04_2016 {
    public static List<Room> getRooms(List<String> input) {
        Objects.requireNonNull(input);
        return input.stream().map(Day04_2016::getRoom).toList();
    }

    public static Room getRoom(String input) {
        Objects.requireNonNull(input);
        // Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash,
        // a sector ID, and a checksum in square brackets.
        Pattern p = Pattern.compile("(?<name>.+)-(?<sector>\\d+)\\[(?<checksum>\\w+)]");

        String name = "";
        int id = -1;
        String checksum = "";

        Matcher matcher = p.matcher(input);
        while (matcher.find()) {
            name = matcher.group("name");
            id = Integer.parseInt(matcher.group("sector"));
            checksum = matcher.group("checksum");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("No name in '" + input + "'");
        }
        if (checksum.isBlank()) {
            throw new IllegalArgumentException("No checksum in '" + input + "'");
        }
        if (id == -1) {
            throw new IllegalArgumentException("No sector ID in '" + input + "'");
        }

        return new Room(name, id, checksum);
    }

    public static Room getRoomWithLetterRotation(String input) {
        Room original = getRoom(input);
        // To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's
        // sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.
        // For example, the real name for <tt>qzmt-zixmtkozy-ivhz-343</tt> is very encrypted name.
        StringBuilder sb = new StringBuilder();
        for (char c : original.encryptedName().toCharArray()) {
            if ('-' == c) {
                sb.append(" ");
            } else {
                sb.append(rotateBy(c, original.sectorID()));
            }
        }
        return new Room(sb.toString(), original.sectorID(), original.checksum());
    }

    private static final char MIN_UPPER = 'A';
    private static final char MAX_UPPER = 'Z';
    private static final char MIN_LOWER = 'a';
    private static final char MAX_LOWER = 'z';

    public static char rotateBy(char in, int rotations) {
        boolean isLowerCase = String.valueOf(in).toLowerCase().equals(String.valueOf(in));
        char min = (isLowerCase) ? MIN_LOWER : MIN_UPPER;
        char max = (isLowerCase) ? MAX_LOWER : MAX_UPPER;

        int distanceFromThisCharToEnd = (int)max - (int)in;

        char result;
        if (rotations > distanceFromThisCharToEnd) {
            result = rotateBy(min, rotations - distanceFromThisCharToEnd - 1); // -1 because we jump from last to first
        } else {
            result = (char) (in + rotations);
        }
        return result;
    }

    public static Map<Character, Long> getLetterCount(String in) {
        Objects.requireNonNull(in);

        return in.codePoints()
                .mapToObj(c -> (char) c)     //Stream<Character>
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}

record Room(String encryptedName, int sectorID, String checksum) {

    /**
     * A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order,
     * with ties broken by alphabetization.
     * <p>
     * For example:
     * <ul>
     * <li><tt>aaaaa-bbb-z-y-x-123[abxyz]</tt> is a real room because the most common letters are a (5), b (3), and then
     * a tie between x, y, and z, which are listed alphabetically.</li>
     * <li><tt>a-b-c-d-e-f-g-h-987[abcde]</tt> is a real room because although the letters are all tied (1 of each), the
     * first five are listed alphabetically.</li>
     * <li><tt>not-a-real-room-404[oarel]</tt> is a real room.</li>
     * <li><tt>totally-real-room-200[decoy]</tt> is not.</li>
     * </ul>
     */
    public boolean isReal() {
        // remove all dashes from encrypted name since they are irrelevant
        String without = encryptedName.replaceAll("-", "");
        Map<Character, Long> letters = getLetterCount(without);

        for (var ch : checksum.toCharArray()) {
            if (!letters.containsKey(ch)) {
                return false;
            }
            if ((long) Collections.max(letters.values()) != letters.get(ch)) {
                return false;
            }
            letters.remove(ch);
        }
        return true;
    }

    public int getID() {
        return sectorID;
    }
}
