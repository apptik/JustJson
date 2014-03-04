/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.djodjo.json.test;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.djodjo.json.JsonArray;
import org.djodjo.json.JsonException;
import org.djodjo.json.JsonTokener;

/**
 * This black box test was written without inspecting the non-free org.json sourcecode.
 */
public class JsonTokenerTest extends TestCase {

    public void testNulls() throws JsonException {
        // JsonTokener accepts null, only to fail later on almost all APIs!
        new JsonTokener(null).back();

        try {
            new JsonTokener(null).more();
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).next();
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).next(3);
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).next('A');
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).nextClean();
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).nextString('"');
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).nextTo('A');
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).nextTo("ABC");
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).nextValue();
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).skipPast("ABC");
            fail();
        } catch (NullPointerException e) {
        }

        try {
            new JsonTokener(null).skipTo('A');
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals("foo! at character 0 of null",
                new JsonTokener(null).syntaxError("foo!").getMessage());

        assertEquals(" at character 0 of null", new JsonTokener(null).toString());
    }

    public void testEmptyString() throws JsonException {
        JsonTokener backTokener = new JsonTokener("");
        backTokener.back();
        assertEquals(" at character 0 of ", backTokener.toString());
        assertFalse(new JsonTokener("").more());
        assertEquals('\0', new JsonTokener("").next());
        try {
            new JsonTokener("").next(3);
            fail();
        } catch (JsonException expected) {
        }
        try {
            new JsonTokener("").next('A');
            fail();
        } catch (JsonException e) {
        }
        assertEquals('\0', new JsonTokener("").nextClean());
        try {
            new JsonTokener("").nextString('"');
            fail();
        } catch (JsonException e) {
        }
        assertEquals("", new JsonTokener("").nextTo('A'));
        assertEquals("", new JsonTokener("").nextTo("ABC"));
        try {
            new JsonTokener("").nextValue();
            fail();
        } catch (JsonException e) {
        }
        new JsonTokener("").skipPast("ABC");
        assertEquals('\0', new JsonTokener("").skipTo('A'));
        assertEquals("foo! at character 0 of ",
                new JsonTokener("").syntaxError("foo!").getMessage());
        assertEquals(" at character 0 of ", new JsonTokener("").toString());
    }

    public void testCharacterNavigation() throws JsonException {
        JsonTokener abcdeTokener = new JsonTokener("ABCDE");
        assertEquals('A', abcdeTokener.next());
        assertEquals('B', abcdeTokener.next('B'));
        assertEquals("CD", abcdeTokener.next(2));
        try {
            abcdeTokener.next(2);
            fail();
        } catch (JsonException e) {
        }
        assertEquals('E', abcdeTokener.nextClean());
        assertEquals('\0', abcdeTokener.next());
        assertFalse(abcdeTokener.more());
        abcdeTokener.back();
        assertTrue(abcdeTokener.more());
        assertEquals('E', abcdeTokener.next());
    }

    public void testBackNextAndMore() throws JsonException {
        JsonTokener abcTokener = new JsonTokener("ABC");
        assertTrue(abcTokener.more());
        abcTokener.next();
        abcTokener.next();
        assertTrue(abcTokener.more());
        abcTokener.next();
        assertFalse(abcTokener.more());
        abcTokener.back();
        assertTrue(abcTokener.more());
        abcTokener.next();
        assertFalse(abcTokener.more());
        abcTokener.back();
        abcTokener.back();
        abcTokener.back();
        abcTokener.back(); // you can back up before the beginning of a String!
        assertEquals('A', abcTokener.next());
    }

    public void testNextMatching() throws JsonException {
        JsonTokener abcdTokener = new JsonTokener("ABCD");
        assertEquals('A', abcdTokener.next('A'));
        try {
            abcdTokener.next('C'); // although it failed, this op consumes a character of input
            fail();
        } catch (JsonException e) {
        }
        assertEquals('C', abcdTokener.next('C'));
        assertEquals('D', abcdTokener.next('D'));
        try {
            abcdTokener.next('E');
            fail();
        } catch (JsonException e) {
        }
    }

    public void testNextN() throws JsonException {
        JsonTokener abcdeTokener = new JsonTokener("ABCDEF");
        assertEquals("", abcdeTokener.next(0));
        try {
            abcdeTokener.next(7);
            fail();
        } catch (JsonException e) {
        }
        assertEquals("ABC", abcdeTokener.next(3));
        try {
            abcdeTokener.next(4);
            fail();
        } catch (JsonException e) {
        }
    }

    public void testNextNWithAllRemaining() throws JsonException {
        JsonTokener tokener = new JsonTokener("ABCDEF");
        tokener.next(3);
        try {
            tokener.next(3);
        } catch (JsonException e) {
            AssertionFailedError error = new AssertionFailedError("off-by-one error?");
            error.initCause(e);
            throw error;
        }
    }

    public void testNext0() throws JsonException {
        JsonTokener tokener = new JsonTokener("ABCDEF");
        tokener.next(5);
        tokener.next();
        try {
            tokener.next(0);
        } catch (JsonException e) {
            Error error = new AssertionFailedError("Returning an empty string should be valid");
            error.initCause(e);
            throw error;
        }
    }

    public void testNextCleanComments() throws JsonException {
        JsonTokener tokener = new JsonTokener(
                "  A  /*XX*/B/*XX//XX\n//XX\nXX*/C//X//X//X\nD/*X*///X\n");
        assertEquals('A', tokener.nextClean());
        assertEquals('B', tokener.nextClean());
        assertEquals('C', tokener.nextClean());
        assertEquals('D', tokener.nextClean());
        assertEquals('\0', tokener.nextClean());
    }

    public void testNextCleanNestedCStyleComments() throws JsonException {
        JsonTokener tokener = new JsonTokener("A /* B /* C */ D */ E");
        assertEquals('A', tokener.nextClean());
        assertEquals('D', tokener.nextClean());
        assertEquals('*', tokener.nextClean());
        assertEquals('/', tokener.nextClean());
        assertEquals('E', tokener.nextClean());
    }

    /**
     * Some applications rely on parsing '#' to lead an end-of-line comment.
     * http://b/2571423
     */
    public void testNextCleanHashComments() throws JsonException {
        JsonTokener tokener = new JsonTokener("A # B */ /* C */ \nD #");
        assertEquals('A', tokener.nextClean());
        assertEquals('D', tokener.nextClean());
        assertEquals('\0', tokener.nextClean());
    }

    public void testNextCleanCommentsTrailingSingleSlash() throws JsonException {
        JsonTokener tokener = new JsonTokener(" / S /");
        assertEquals('/', tokener.nextClean());
        assertEquals('S', tokener.nextClean());
        assertEquals('/', tokener.nextClean());
        assertEquals("nextClean doesn't consume a trailing slash",
                '\0', tokener.nextClean());
    }

    public void testNextCleanTrailingOpenComment() throws JsonException {
        try {
            new JsonTokener("  /* ").nextClean();
            fail();
        } catch (JsonException e) {
        }
        assertEquals('\0', new JsonTokener("  // ").nextClean());
    }

    public void testNextCleanNewlineDelimiters() throws JsonException {
        assertEquals('B', new JsonTokener("  // \r\n  B ").nextClean());
        assertEquals('B', new JsonTokener("  // \n  B ").nextClean());
        assertEquals('B', new JsonTokener("  // \r  B ").nextClean());
    }

    public void testNextCleanSkippedWhitespace() throws JsonException {
        assertEquals("character tabulation", 'A', new JsonTokener("\tA").nextClean());
        assertEquals("line feed",            'A', new JsonTokener("\nA").nextClean());
        assertEquals("carriage return",      'A', new JsonTokener("\rA").nextClean());
        assertEquals("space",                'A', new JsonTokener(" A").nextClean());
    }

    /**
     * Tests which characters tokener treats as ignorable whitespace. See Kevin Bourrillion's
     * <a href="https://spreadsheets.google.com/pub?key=pd8dAQyHbdewRsnE5x5GzKQ">list
     * of whitespace characters</a>.
     */
    public void testNextCleanRetainedWhitespace() throws JsonException {
        assertNotClean("null",                      '\u0000');
        assertNotClean("next line",                 '\u0085');
        assertNotClean("non-breaking space",        '\u00a0');
        assertNotClean("ogham space mark",          '\u1680');
        assertNotClean("mongolian vowel separator", '\u180e');
        assertNotClean("en quad",                   '\u2000');
        assertNotClean("em quad",                   '\u2001');
        assertNotClean("en space",                  '\u2002');
        assertNotClean("em space",                  '\u2003');
        assertNotClean("three-per-em space",        '\u2004');
        assertNotClean("four-per-em space",         '\u2005');
        assertNotClean("six-per-em space",          '\u2006');
        assertNotClean("figure space",              '\u2007');
        assertNotClean("punctuation space",         '\u2008');
        assertNotClean("thin space",                '\u2009');
        assertNotClean("hair space",                '\u200a');
        assertNotClean("zero-width space",          '\u200b');
        assertNotClean("left-to-right mark",        '\u200e');
        assertNotClean("right-to-left mark",        '\u200f');
        assertNotClean("line separator",            '\u2028');
        assertNotClean("paragraph separator",       '\u2029');
        assertNotClean("narrow non-breaking space", '\u202f');
        assertNotClean("medium mathematical space", '\u205f');
        assertNotClean("ideographic space",         '\u3000');
        assertNotClean("line tabulation",           '\u000b');
        assertNotClean("form feed",                 '\u000c');
        assertNotClean("information separator 4",   '\u001c');
        assertNotClean("information separator 3",   '\u001d');
        assertNotClean("information separator 2",   '\u001e');
        assertNotClean("information separator 1",   '\u001f');
    }

    private void assertNotClean(String name, char c) throws JsonException {
        assertEquals("The character " + name + " is not whitespace according to the Json spec.",
                c, new JsonTokener(new String(new char[] { c, 'A' })).nextClean());
    }

    public void testNextString() throws JsonException {
        assertEquals("", new JsonTokener("'").nextString('\''));
        assertEquals("", new JsonTokener("\"").nextString('\"'));
        assertEquals("ABC", new JsonTokener("ABC'DEF").nextString('\''));
        assertEquals("ABC", new JsonTokener("ABC'''DEF").nextString('\''));

        // nextString permits slash-escaping of arbitrary characters!
        assertEquals("ABC", new JsonTokener("A\\B\\C'DEF").nextString('\''));

        JsonTokener tokener = new JsonTokener(" 'abc' 'def' \"ghi\"");
        tokener.next();
        assertEquals('\'', tokener.next());
        assertEquals("abc", tokener.nextString('\''));
        tokener.next();
        assertEquals('\'', tokener.next());
        assertEquals("def", tokener.nextString('\''));
        tokener.next();
        assertEquals('"', tokener.next());
        assertEquals("ghi", tokener.nextString('\"'));
        assertFalse(tokener.more());
    }

    public void testNextStringNoDelimiter() throws JsonException {
        try {
            new JsonTokener("").nextString('\'');
            fail();
        } catch (JsonException e) {
        }

        JsonTokener tokener = new JsonTokener(" 'abc");
        tokener.next();
        tokener.next();
        try {
            tokener.next('\'');
            fail();
        } catch (JsonException e) {
        }
    }

    public void testNextStringEscapedQuote() throws JsonException {
        try {
            new JsonTokener("abc\\").nextString('"');
            fail();
        } catch (JsonException e) {
        }

        // we're mixing Java escaping like \" and JavaScript escaping like \\\"
        // which makes these tests extra tricky to read!
        assertEquals("abc\"def", new JsonTokener("abc\\\"def\"ghi").nextString('"'));
        assertEquals("abc\\def", new JsonTokener("abc\\\\def\"ghi").nextString('"'));
        assertEquals("abc/def", new JsonTokener("abc\\/def\"ghi").nextString('"'));
        assertEquals("abc\bdef", new JsonTokener("abc\\bdef\"ghi").nextString('"'));
        assertEquals("abc\fdef", new JsonTokener("abc\\fdef\"ghi").nextString('"'));
        assertEquals("abc\ndef", new JsonTokener("abc\\ndef\"ghi").nextString('"'));
        assertEquals("abc\rdef", new JsonTokener("abc\\rdef\"ghi").nextString('"'));
        assertEquals("abc\tdef", new JsonTokener("abc\\tdef\"ghi").nextString('"'));
    }

    public void testNextStringUnicodeEscaped() throws JsonException {
        // we're mixing Java escaping like \\ and JavaScript escaping like \\u
        assertEquals("abc def", new JsonTokener("abc\\u0020def\"ghi").nextString('"'));
        assertEquals("abcU0020def", new JsonTokener("abc\\U0020def\"ghi").nextString('"'));

        // Json requires 4 hex characters after a unicode escape
        try {
            new JsonTokener("abc\\u002\"").nextString('"');
            fail();
        } catch (NumberFormatException e) {
        } catch (JsonException e) {
        }
        try {
            new JsonTokener("abc\\u").nextString('"');
            fail();
        } catch (JsonException e) {
        }
        try {
            new JsonTokener("abc\\u    \"").nextString('"');
            fail();
        } catch (NumberFormatException e) {
        }
        assertEquals("abc\"def", new JsonTokener("abc\\u0022def\"ghi").nextString('"'));
        try {
            new JsonTokener("abc\\u000G\"").nextString('"');
            fail();
        } catch (NumberFormatException e) {
        }
    }

    public void testNextStringNonQuote() throws JsonException {
        assertEquals("AB", new JsonTokener("ABC").nextString('C'));
        assertEquals("ABCD", new JsonTokener("AB\\CDC").nextString('C'));
        assertEquals("AB\nC", new JsonTokener("AB\\nCn").nextString('n'));
    }

    public void testNextTo() throws JsonException {
        assertEquals("ABC", new JsonTokener("ABCDEFG").nextTo("DHI"));
        assertEquals("ABCDEF", new JsonTokener("ABCDEF").nextTo(""));

        JsonTokener tokener = new JsonTokener("ABC\rDEF\nGHI\r\nJKL");
        assertEquals("ABC", tokener.nextTo("M"));
        assertEquals('\r', tokener.next());
        assertEquals("DEF", tokener.nextTo("M"));
        assertEquals('\n', tokener.next());
        assertEquals("GHI", tokener.nextTo("M"));
        assertEquals('\r', tokener.next());
        assertEquals('\n', tokener.next());
        assertEquals("JKL", tokener.nextTo("M"));

        tokener = new JsonTokener("ABCDEFGHI");
        assertEquals("ABC", tokener.nextTo("DEF"));
        assertEquals("", tokener.nextTo("DEF"));
        assertEquals('D', tokener.next());
        assertEquals("", tokener.nextTo("DEF"));
        assertEquals('E', tokener.next());
        assertEquals("", tokener.nextTo("DEF"));
        assertEquals('F', tokener.next());
        assertEquals("GHI", tokener.nextTo("DEF"));
        assertEquals("", tokener.nextTo("DEF"));

        tokener = new JsonTokener(" \t \fABC \t DEF");
        assertEquals("ABC", tokener.nextTo("DEF"));
        assertEquals('D', tokener.next());

        tokener = new JsonTokener(" \t \fABC \n DEF");
        assertEquals("ABC", tokener.nextTo("\n"));
        assertEquals("", tokener.nextTo("\n"));

        tokener = new JsonTokener("");
        try {
            tokener.nextTo(null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    public void testNextToTrimming() {
        assertEquals("ABC", new JsonTokener("\t ABC \tDEF").nextTo("DE"));
        assertEquals("ABC", new JsonTokener("\t ABC \tDEF").nextTo('D'));
    }

    public void testNextToTrailing() {
        assertEquals("ABC DEF", new JsonTokener("\t ABC DEF \t").nextTo("G"));
        assertEquals("ABC DEF", new JsonTokener("\t ABC DEF \t").nextTo('G'));
    }

    public void testNextToDoesntStopOnNull() {
        String message = "nextTo() shouldn't stop after \\0 characters";
        JsonTokener tokener = new JsonTokener(" \0\t \fABC \n DEF");
        assertEquals(message, "ABC", tokener.nextTo("D"));
        assertEquals(message, '\n', tokener.next());
        assertEquals(message, "", tokener.nextTo("D"));
    }

    public void testNextToConsumesNull() {
        String message = "nextTo shouldn't consume \\0.";
        JsonTokener tokener = new JsonTokener("ABC\0DEF");
        assertEquals(message, "ABC", tokener.nextTo("\0"));
        assertEquals(message, '\0', tokener.next());
        assertEquals(message, "DEF", tokener.nextTo("\0"));
    }

    public void testSkipPast() {
        JsonTokener tokener = new JsonTokener("ABCDEF");
        tokener.skipPast("ABC");
        assertEquals('D', tokener.next());
        tokener.skipPast("EF");
        assertEquals('\0', tokener.next());

        tokener = new JsonTokener("ABCDEF");
        tokener.skipPast("ABCDEF");
        assertEquals('\0', tokener.next());

        tokener = new JsonTokener("ABCDEF");
        tokener.skipPast("G");
        assertEquals('\0', tokener.next());

        tokener = new JsonTokener("ABC\0ABC");
        tokener.skipPast("ABC");
        assertEquals('\0', tokener.next());
        assertEquals('A', tokener.next());

        tokener = new JsonTokener("\0ABC");
        tokener.skipPast("ABC");
        assertEquals('\0', tokener.next());

        tokener = new JsonTokener("ABC\nDEF");
        tokener.skipPast("DEF");
        assertEquals('\0', tokener.next());

        tokener = new JsonTokener("ABC");
        tokener.skipPast("ABCDEF");
        assertEquals('\0', tokener.next());

        tokener = new JsonTokener("ABCDABCDABCD");
        tokener.skipPast("ABC");
        assertEquals('D', tokener.next());
        tokener.skipPast("ABC");
        assertEquals('D', tokener.next());
        tokener.skipPast("ABC");
        assertEquals('D', tokener.next());

        tokener = new JsonTokener("");
        try {
            tokener.skipPast(null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    public void testSkipTo() {
        JsonTokener tokener = new JsonTokener("ABCDEF");
        tokener.skipTo('A');
        assertEquals('A', tokener.next());
        tokener.skipTo('D');
        assertEquals('D', tokener.next());
        tokener.skipTo('G');
        assertEquals('E', tokener.next());
        tokener.skipTo('A');
        assertEquals('F', tokener.next());

        tokener = new JsonTokener("ABC\nDEF");
        tokener.skipTo('F');
        assertEquals('F', tokener.next());

        tokener = new JsonTokener("ABCfDEF");
        tokener.skipTo('F');
        assertEquals('F', tokener.next());

        tokener = new JsonTokener("ABC/* DEF */");
        tokener.skipTo('D');
        assertEquals('D', tokener.next());
    }

    public void testSkipToStopsOnNull() {
        JsonTokener tokener = new JsonTokener("ABC\0DEF");
        tokener.skipTo('F');
        assertEquals("skipTo shouldn't stop when it sees '\\0'", 'F', tokener.next());
    }

    public void testBomIgnoredAsFirstCharacterOfDocument() throws JsonException {
        JsonTokener tokener = new JsonTokener("\ufeff[]");
        JsonArray array = (JsonArray) tokener.nextValue();
        assertEquals(0, array.length());
    }

    public void testBomTreatedAsCharacterInRestOfDocument() throws JsonException {
        JsonTokener tokener = new JsonTokener("[\ufeff]");
        JsonArray array = (JsonArray) tokener.nextValue();
        assertEquals(1, array.length());
    }

    public void testDehexchar() {
        assertEquals( 0, JsonTokener.dehexchar('0'));
        assertEquals( 1, JsonTokener.dehexchar('1'));
        assertEquals( 2, JsonTokener.dehexchar('2'));
        assertEquals( 3, JsonTokener.dehexchar('3'));
        assertEquals( 4, JsonTokener.dehexchar('4'));
        assertEquals( 5, JsonTokener.dehexchar('5'));
        assertEquals( 6, JsonTokener.dehexchar('6'));
        assertEquals( 7, JsonTokener.dehexchar('7'));
        assertEquals( 8, JsonTokener.dehexchar('8'));
        assertEquals( 9, JsonTokener.dehexchar('9'));
        assertEquals(10, JsonTokener.dehexchar('A'));
        assertEquals(11, JsonTokener.dehexchar('B'));
        assertEquals(12, JsonTokener.dehexchar('C'));
        assertEquals(13, JsonTokener.dehexchar('D'));
        assertEquals(14, JsonTokener.dehexchar('E'));
        assertEquals(15, JsonTokener.dehexchar('F'));
        assertEquals(10, JsonTokener.dehexchar('a'));
        assertEquals(11, JsonTokener.dehexchar('b'));
        assertEquals(12, JsonTokener.dehexchar('c'));
        assertEquals(13, JsonTokener.dehexchar('d'));
        assertEquals(14, JsonTokener.dehexchar('e'));
        assertEquals(15, JsonTokener.dehexchar('f'));

        for (int c = 0; c <= 0xFFFF; c++) {
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')) {
                continue;
            }
            assertEquals("dehexchar " + c, -1, JsonTokener.dehexchar((char) c));
        }
    }
}
