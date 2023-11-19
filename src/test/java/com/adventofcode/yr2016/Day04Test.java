package com.adventofcode.yr2016;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.adventofcode.yr2016.Day04_2016.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted and full of
 * decoy data, but the instructions to decode the list are barely hidden nearby. Better remove the decoy data first.
 * <p>
 * Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID,
 * and a checksum in square brackets.
 * <p>
 * A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order, with
 * ties broken by alphabetization.
 */
public class Day04Test {

    /**
     * For example:
     * <ul>
     * <li><tt>aaaaa-bbb-z-y-x-123[abxyz]</tt> is a real room because the most common letters are a (5), b (3), and then
     * a tie between x, y, and z, which are listed alphabetically.</li>
     * <li><tt>a-b-c-d-e-f-g-h-987[abcde]</tt> is a real room because although the letters are all tied (1 of each), the
     * first five are listed alphabetically.</li>
     * <li><tt>not-a-real-room-404[oarel]</tt> is a real room.</li>
     * <li><tt>totally-real-room-200[decoy]</tt> is not.</li>
     * </ul>
     * Of the real rooms from the list above, the sum of their sector IDs is 1514.
     */
    @Test
    void example1() {
        var input = List.of(
                "aaaaa-bbb-z-y-x-123[abxyz]",
                "a-b-c-d-e-f-g-h-987[abcde]",
                "not-a-real-room-404[oarel]",
                "totally-real-room-200[decoy]"
        );
        var rooms = getRooms(input);
        int sum = rooms.stream().filter(Room::isReal).mapToInt(Room::getID).sum();
        assertThat(sum).isEqualTo(1514);
    }

    @Test
    void example1_1() {
        var room = getRoom("aaaaa-bbb-z-y-x-123[abxyz]");
        assertThat(room.isReal()).isTrue();
        assertThat(room.getID()).isEqualTo(123);
    }

    @Test
    void example1_2() {
        var room = getRoom("a-b-c-d-e-f-g-h-987[abcde]");
        assertThat(room.isReal()).isTrue();
        assertThat(room.getID()).isEqualTo(987);
    }

    @Test
    void example1_3() {
        var room = getRoom("not-a-real-room-404[oarel]");
        assertThat(room.isReal()).isTrue();
        assertThat(room.getID()).isEqualTo(404);
    }

    @Test
    void example1_4() {
        var room = getRoom("totally-real-room-200[decoy]");
        assertThat(room.isReal()).isFalse();
    }

    /**
     * What is the sum of the sector IDs of the real rooms?
     */
    @Test
    void riddle1() {
        String fileName = "2016_04.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var rooms = getRooms(lines);
        int sum = rooms.stream().filter(Room::isReal).mapToInt(Room::getID).sum();
        assertThat(sum).isEqualTo(409147);
    }

    /**
     * With all the decoy data out of the way, it's time to decrypt this list and get moving.
     * <p>
     * The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without the right
     * software. However, the information kiosk designers at Easter Bunny HQ were not expecting to deal with a master
     * cryptographer like yourself.
     * <p>
     * To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's
     * sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.
     * <p>
     * For example, the real name for "qzmt-zixmtkozy-ivhz-343" is "very encrypted name".
     * <p>
     * What is the sector ID of the room where North Pole objects are stored?
     */
    @Test
    void riddle2() {
        String fileName = "2016_04.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var rooms = lines.stream().map(Day04_2016::getRoomWithLetterRotation).toList();
        var result = rooms.stream()
                .filter(r -> r.encryptedName().contains("northpole"))
                .filter(r -> r.encryptedName().contains("object"))
                .filter(r -> r.encryptedName().contains("storage"))
                .findFirst().orElseThrow();
        assertThat(result.getID()).isEqualTo(991);
    }

    /**
     * To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's
     * sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.
     * <p>
     * For example, the real name for <tt>qzmt-zixmtkozy-ivhz-343</tt> is <tt>very encrypted name</tt>.
     */
    @Test
    void example2() {
        var room = getRoomWithLetterRotation("qzmt-zixmtkozy-ivhz-343[zmti]");
        assertThat(room.encryptedName()).isEqualTo("very encrypted name");
    }

    /**
     * A becomes B, B becomes C, Z becomes A, and so on.
     */
    @Test
    void example2_1() {
        assertThat(rotateBy('A', 1)).isEqualTo('B');
        assertThat(rotateBy('a', 1)).isEqualTo('b');
        assertThat(rotateBy('B', 1)).isEqualTo('C');
        assertThat(rotateBy('b', 1)).isEqualTo('c');

        assertThat(rotateBy('A', 3)).isEqualTo('D');
        assertThat(rotateBy('a', 3)).isEqualTo('d');

        // overflow
        assertThat(rotateBy('Z', 1)).isEqualTo('A');
        assertThat(rotateBy('z', 1)).isEqualTo('a');
        assertThat(rotateBy('X', 3)).isEqualTo('A');
        assertThat(rotateBy('x', 3)).isEqualTo('a');

        assertThat(rotateBy('Z', 5)).isEqualTo('E');
        assertThat(rotateBy('z', 5)).isEqualTo('e');

        // ascii has 26 letters
        assertThat(rotateBy('Z', 52 + 5)).isEqualTo('E');
        assertThat(rotateBy('z', 52 + 5)).isEqualTo('e');
    }
}
