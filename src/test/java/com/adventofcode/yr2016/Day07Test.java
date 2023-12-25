package com.adventofcode.yr2016;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7, of course; IPv6 is
 * much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).
 */
public class Day07Test {
    /**
     * For example:
     * <ul>
     * <li><tt>abba[mnop]qrst</tt> supports TLS (abba outside square brackets).</li>
     * <li><tt>abcd[bddb]xyyx</tt> does not support TLS (bddb is within square brackets, even though xyyx is outside
     *     square brackets).</li>
     * <li><tt>aaaa[qwer]tyui</tt> does not support TLS (aaaa is invalid; the interior characters must be different).</li>
     * <li><tt>ioxxoj[asdfgh]zxcvbn</tt> supports TLS (oxxo is outside square brackets, even though it's within a
     *     larger string).</li>
     * </ul>
     */
    @Test
    void example1() {
        assertThat(supportsTls("abba[mnop]qrst")).isTrue();
        assertThat(supportsTls("abcd[bddb]xyyx")).isFalse();
        assertThat(supportsTls("aaaa[qwer]tyui")).isFalse();
        assertThat(supportsTls("ioxxoj[asdfgh]zxcvbn")).isTrue();
    }

    /**
     * How many IPs in your puzzle input support TLS?
     */
    @Test
    void riddle1() {
        String fileName = "2016_07.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var validIPaddresses = getIPsWithTLS(lines);
        int result = validIPaddresses.size();

        assertThat(result).isEqualTo(105);
    }

    private List<String> getIPsWithTLS(List<String> lines) {
        return lines.stream().filter(this::supportsTls).toList();
    }

    /**
     * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any four-character sequence
     * which consists of a pair of two different characters followed by the reverse of that pair, such as xyyx or abba.
     * However, the IP also must not have an ABBA within any hypernet sequences, which are contained by square brackets.
     */
    private boolean supportsTls(String ip) {
        var split = splitIp(ip);

        boolean containsABBAinsideSquareBrackets = Arrays.stream(split.insideBrackets).anyMatch(this::containsABBA);
        if (!containsABBAinsideSquareBrackets) {
            boolean result = Arrays.stream(split.outside).anyMatch(this::containsABBA);
            if (result) {
                return result;
            }
        }
        return false;
    }

    private Pair splitIp(String ip) {
        var parts1 = ip.split("\\[");
        var parts2 = new String[parts1.length * 2 - 1];
        parts2[0] = parts1[0];
        int count = 1;
        for (int i = 1; i < parts1.length; i++) {
            var split = parts1[i].split("]");
            parts2[count] = split[0];
            count++;
            parts2[count] = split[1];
            count++;
        }

        var insideBrackets = new String[parts2.length / 2];
        var outside = new String[parts2.length / 2 + 1];
        int even = 0;
        int odd = 0;
        for (int i = 0; i < parts2.length; i++) {
            if (i % 2 == 0) { // array count start with 0, so even index is odd element
                outside[odd++] = parts2[i];
            } else {
                insideBrackets[even++] = parts2[i];
            }
        }
        return new Pair(insideBrackets, outside);
    }

    record Pair(String[] insideBrackets, String[] outside){};

    /**
     * An ABBA is any four-character sequence which consists of a pair of two different characters followed by the
     * reverse of that pair, such as xyyx or abba.
     */
    private boolean containsABBA(String s) {
        var split = s.toCharArray();
        boolean result = false;
        for (int i = 0; i < split.length - 3; i++) {
            // no same char ("aa" or "zz") allowed
            if (split[i] != split[i+1]) {
                String thisPair = String.valueOf(split[i]) + String.valueOf(split[i+1]);
                String otherPairInversed = String.valueOf(split[i+3]) + String.valueOf(split[i+2]);
                if (thisPair.equals(otherPairInversed)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * You would also like to know which IPs support SSL (super-secret listening).
     * <p>
     * For example:
     * <ul>
     * <li>aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).</li>
     * <li>xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).</li>
     * <li>aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is not related, because the interior character must be different).</li>
     * <li>zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, even though zaz and zbz overlap).</li>
     * </ul>
     */
    @Test
    void example2() {
        assertThat(supportsSsl2("aba[bab]xyz")).isTrue();
        assertThat(supportsSsl2("xyx[xyx]xyx")).isFalse();
        assertThat(supportsSsl2("aaa[kek]eke")).isTrue();
        assertThat(supportsSsl2("zazbz[bzb]cdb")).isTrue();
    }

    /**
     * How many IPs in your puzzle input support SSL?
     */
    @Test
    void riddle2() {
        String fileName = "2016_07.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var validIPaddresses = lines.stream().filter(this::supportsSsl2).toList();
        int result = validIPaddresses.size();

        assertThat(result).isEqualTo(258);
    }

    /**
     * An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences (outside any
     * square bracketed sections), and a corresponding Byte Allocation Block, or BAB, anywhere in the hypernet sequences.
     * An ABA is any three-character sequence which consists of the same character twice with a different character
     * between them, such as xyx or aba. A corresponding BAB is the same characters but in reversed positions:
     * yxy and bab, respectively.
     */
    private boolean supportsSsl(String ip) {
        var split = splitIp(ip);

        var ABAinsideSquareBrackets = Arrays.stream(split.insideBrackets).filter(this::containsABA).toList();
        for (String s : ABAinsideSquareBrackets) {
            var aba = findABA(s).toCharArray();
            var bab = String.valueOf(aba[1]) + String.valueOf(aba[0]) + String.valueOf(aba[1]);
            var result = Arrays.stream(split.outside).anyMatch(o -> o.contains(bab));
            if (result) {
                System.out.println(ip + " --> " + bab);
                return true;
            }
        }
        return false;
    }

    private boolean supportsSsl2(String ip) {
        var split = splitIp(ip);

        Set<String> matches = new HashSet<>();
        for (String inside : split.insideBrackets) {
            while (inside.length() >= 3) {
                String match = findABA(inside);
                if (!match.isBlank()) {
                    matches.add(match);
                }
                inside = inside.substring(1);
            }
        }

        var inverted = matches.stream().map(s -> {
            var x = s.toCharArray();
            return String.valueOf(x[1]) + String.valueOf(x[0]) + String.valueOf(x[1]);
        }).toList();

        for (String outside : split.outside) {
            for (String x : inverted) {
                if (outside.contains(x)) {
                    System.out.println(x + " --> " + outside);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * An ABA is any three-character sequence which consists of the same character twice with a different character
     * between them, such as xyx or aba
     */
    private boolean containsABA(String s) {
        return (!findABA(s).equals(""));
    }

    private String findABA(String s) {
        var split = s.toCharArray();
        String result = "";
        for (int i = 0; i <= split.length - 3; i++) {
            // no same char ("aa" or "zz") allowed
            if (split[i] != split[i + 1]) {
                if (String.valueOf(split[i]).equals(String.valueOf(split[i + 2]))) {
                    result += String.valueOf(split[i]) + String.valueOf(split[i + 1]) + String.valueOf(split[i + 2]);
                    break;
                }
            }
        }

        return result;
    }
}
