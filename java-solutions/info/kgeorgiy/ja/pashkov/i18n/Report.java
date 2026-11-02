package info.kgeorgiy.ja.pashkov.i18n;

import java.util.Date;
import java.util.List;
import java.util.Locale;

record Report(
        List<String> sentences,
        List<String> words,
        List<Double> numbers,
        List<Double> currencies,
        List<Date> dates,
        Locale textLocale
) {
}

//Error for gen-floats.misc/en-US/ru-RU in 23 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < ������� ����� �����������: 1162,0.> but was: < ������� ����� �����������: 1�162,0.>
//Testing gen-floats.misc/fr-FR/ru-RU
//Error for gen-floats.misc/ru-RU/en-US in 24 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < Average sentence length: 1146.0.> but was: < Average sentence length: 1,146.0.>
//Testing gen-floats.misc/fr-FR/en-US
//Error for gen-floats.misc/ru-RU/ru-RU in 25 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < ������� ����� �����������: 1146,0.> but was: < ������� ����� �����������: 1�146,0.>
//Testing gen-floats.misc/zh-CN/ru-RU
//Error for gen-floats.misc/en-US/en-US in 26 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < Average sentence length: 1162.0.> but was: < Average sentence length: 1,162.0.>
//Testing gen-floats.misc/zh-CN/en-US
//Error for gen-floats.misc/zh-CN/ru-RU in 19 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < ������� ����� �����������: 1131,0.> but was: < ������� ����� �����������: 1�131,0.>
//Testing gen-floats.misc/ar-MA/ru-RU
//Error for gen-floats.misc/zh-CN/en-US in 19 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < Average sentence length: 1131.0.> but was: < Average sentence length: 1,131.0.>
//Testing gen-floats.misc/ar-MA/en-US
//Error for gen-floats.misc/fr-FR/en-US in 31 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < Average sentence length: 1133.0.> but was: < Average sentence length: 1,133.0.>
//Testing gen-floats.misc/he/ru-RU
//Error for gen-floats.misc/ar-MA/en-US in 20 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < Average sentence length: 1207.0.> but was: < Average sentence length: 1,207.0.>
//Testing gen-floats.misc/he/en-US
//Error for gen-floats.misc/ar-MA/ru-RU in 22 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < ������� ����� �����������: 1207,0.> but was: < ������� ����� �����������: 1�207,0.>
//Error for gen-floats.misc/fr-FR/ru-RU in 56 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < ������� ����� �����������: 1133,0.> but was: < ������� ����� �����������: 1�133,0.>
//Error for gen-floats.misc/he/ru-RU in 34 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < ������� ����� �����������: 1182,0.> but was: < ������� ����� �����������: 1�182,0.>
//Error for gen-floats.misc/he/en-US in 24 ms: (AssertionFailedError) For gen-floats.misc: line 14 ==> expected: < Average sentence length: 1182.0.> but was: < Average sentence length: 1,182.0.>
//Testing gen-int-currency.lines/en-US/ru-RU
//Testing gen-int-currency.lines/en-US/en-US
//Testing gen-int-currency.lines/ru-RU/ru-RU
//Testing gen-int-currency.lines/ru-RU/en-US
//Error for gen-int-currency.lines/en-US/ru-RU in 22 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1238,0.> but was: < ������� ����� �����������: 1�238,0.>
//Testing gen-int-currency.lines/fr-FR/ru-RU
//Error for gen-int-currency.lines/en-US/en-US in 22 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < Average sentence length: 1238.0.> but was: < Average sentence length: 1,238.0.>
//Testing gen-int-currency.lines/fr-FR/en-US
//Error for gen-int-currency.lines/ru-RU/en-US in 23 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < Average sentence length: 1338.0.> but was: < Average sentence length: 1,338.0.>
//Testing gen-int-currency.lines/zh-CN/ru-RU
//Error for gen-int-currency.lines/ru-RU/ru-RU in 23 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1338,0.> but was: < ������� ����� �����������: 1�338,0.>
//Testing gen-int-currency.lines/zh-CN/en-US
//Error for gen-int-currency.lines/fr-FR/en-US in 20 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < Average sentence length: 1338.0.> but was: < Average sentence length: 1,338.0.>
//Testing gen-int-currency.lines/ar-MA/ru-RU
//Error for gen-int-currency.lines/zh-CN/ru-RU in 19 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1238,0.> but was: < ������� ����� �����������: 1�238,0.>
//Testing gen-int-currency.lines/ar-MA/en-US
//Error for gen-int-currency.lines/zh-CN/en-US in 20 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < Average sentence length: 1238.0.> but was: < Average sentence length: 1,238.0.>
//Testing gen-int-currency.lines/he/ru-RU
//Error for gen-int-currency.lines/ar-MA/ru-RU in 22 ms: (AssertionFailedError) For gen-int-currency.lines: line 29 ==> expected: < ����������� �����: -961�589,80�?.> but was: < ����������� �����: -961589,80�?.>
//Testing gen-int-currency.lines/he/en-US
//Error for gen-int-currency.lines/ar-MA/en-US in 23 ms: (AssertionFailedError) For gen-int-currency.lines: line 29 ==> expected: < Minimal currency: -$961,589.80.> but was: < Minimal currency: -$961589.80.>
//Error for gen-int-currency.lines/fr-FR/ru-RU in 52 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1338,0.> but was: < ������� ����� �����������: 1�338,0.>
//Error for gen-int-currency.lines/he/ru-RU in 32 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1587,0.> but was: < ������� ����� �����������: 1�587,0.>
//Error for gen-int-currency.lines/he/en-US in 18 ms: (AssertionFailedError) For gen-int-currency.lines: line 14 ==> expected: < Average sentence length: 1587.0.> but was: < Average sentence length: 1,587.0.>
//Testing gen-int-currency.spaces/en-US/ru-RU
//Testing gen-int-currency.spaces/en-US/en-US
//Testing gen-int-currency.spaces/ru-RU/ru-RU
//Testing gen-int-currency.spaces/ru-RU/en-US
//Error for gen-int-currency.spaces/en-US/ru-RU in 25 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1398,0.> but was: < ������� ����� �����������: 1�398,0.>
//Testing gen-int-currency.spaces/fr-FR/ru-RU
//Error for gen-int-currency.spaces/en-US/en-US in 24 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < Average sentence length: 1398.0.> but was: < Average sentence length: 1,398.0.>
//Testing gen-int-currency.spaces/fr-FR/en-US
//Error for gen-int-currency.spaces/ru-RU/ru-RU in 33 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1502,0.> but was: < ������� ����� �����������: 1�502,0.>
//Testing gen-int-currency.spaces/zh-CN/ru-RU
//Error for gen-int-currency.spaces/ru-RU/en-US in 33 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < Average sentence length: 1502.0.> but was: < Average sentence length: 1,502.0.>
//Testing gen-int-currency.spaces/zh-CN/en-US
//Error for gen-int-currency.spaces/fr-FR/en-US in 23 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < Average sentence length: 1490.0.> but was: < Average sentence length: 1,490.0.>
//Testing gen-int-currency.spaces/ar-MA/ru-RU
//Error for gen-int-currency.spaces/zh-CN/ru-RU in 20 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1384,0.> but was: < ������� ����� �����������: 1�384,0.>
//Testing gen-int-currency.spaces/ar-MA/en-US
//Error for gen-int-currency.spaces/zh-CN/en-US in 20 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < Average sentence length: 1384.0.> but was: < Average sentence length: 1,384.0.>
//Testing gen-int-currency.spaces/he/ru-RU
//Error for gen-int-currency.spaces/fr-FR/ru-RU in 30 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1490,0.> but was: < ������� ����� �����������: 1�490,0.>
//Testing gen-int-currency.spaces/he/en-US
//Error for gen-int-currency.spaces/ar-MA/en-US in 24 ms: (AssertionFailedError) For gen-int-currency.spaces: line 29 ==> expected: < Minimal currency: -$961,589.80.> but was: < Minimal currency: -$961589.80.>
//Error for gen-int-currency.spaces/he/ru-RU in 24 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1729,0.> but was: < ������� ����� �����������: 1�729,0.>
//Error for gen-int-currency.spaces/he/en-US in 23 ms: (AssertionFailedError) For gen-int-currency.spaces: line 14 ==> expected: < Average sentence length: 1729.0.> but was: < Average sentence length: 1,729.0.>
//Error for gen-int-currency.spaces/ar-MA/ru-RU in 55 ms: (AssertionFailedError) For gen-int-currency.spaces: line 29 ==> expected: < ����������� �����: -961�589,80�?.> but was: < ����������� �����: -961589,80�?.>
//Testing gen-int-currency.words/en-US/ru-RU
//Testing gen-int-currency.words/en-US/en-US
//Testing gen-int-currency.words/ru-RU/ru-RU
//Testing gen-int-currency.words/ru-RU/en-US
//Error for gen-int-currency.words/ru-RU/en-US in 26 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < Average sentence length: 1785.0.> but was: < Average sentence length: 1,785.0.>
//Testing gen-int-currency.words/fr-FR/ru-RU
//Error for gen-int-currency.words/en-US/en-US in 26 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < Average sentence length: 1670.0.> but was: < Average sentence length: 1,670.0.>
//Testing gen-int-currency.words/fr-FR/en-US
//Error for gen-int-currency.words/ru-RU/ru-RU in 26 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < ������� ����� �����������: 1785,0.> but was: < ������� ����� �����������: 1�785,0.>
//Testing gen-int-currency.words/zh-CN/ru-RU
//Error for gen-int-currency.words/en-US/ru-RU in 28 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < ������� ����� �����������: 1670,0.> but was: < ������� ����� �����������: 1�670,0.>
//Testing gen-int-currency.words/zh-CN/en-US
//Error for gen-int-currency.words/fr-FR/ru-RU in 21 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < ������� ����� �����������: 1782,0.> but was: < ������� ����� �����������: 1�782,0.>
//Testing gen-int-currency.words/ar-MA/ru-RU
//Error for gen-int-currency.words/fr-FR/en-US in 21 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < Average sentence length: 1782.0.> but was: < Average sentence length: 1,782.0.>
//Testing gen-int-currency.words/ar-MA/en-US
//Error for gen-int-currency.words/zh-CN/ru-RU in 29 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < ������� ����� �����������: 1673,0.> but was: < ������� ����� �����������: 1�673,0.>
//Testing gen-int-currency.words/he/ru-RU
//Error for gen-int-currency.words/zh-CN/en-US in 28 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < Average sentence length: 1673.0.> but was: < Average sentence length: 1,673.0.>
//Testing gen-int-currency.words/he/en-US
//Error for gen-int-currency.words/ar-MA/ru-RU in 19 ms: (AssertionFailedError) For gen-int-currency.words: line 29 ==> expected: < ����������� �����: -961�589,80�?.> but was: < ����������� �����: -961589,80�?.>
//Error for gen-int-currency.words/ar-MA/en-US in 25 ms: (AssertionFailedError) For gen-int-currency.words: line 29 ==> expected: < Minimal currency: -$961,589.80.> but was: < Minimal currency: -$961589.80.>
//Error for gen-int-currency.words/he/ru-RU in 24 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < ������� ����� �����������: 2047,0.> but was: < ������� ����� �����������: 2�047,0.>
//Error for gen-int-currency.words/he/en-US in 28 ms: (AssertionFailedError) For gen-int-currency.words: line 14 ==> expected: < Average sentence length: 2047.0.> but was: < Average sentence length: 2,047.0.>
//Testing gen-int-currency.digits/en-US/ru-RU
//Testing gen-int-currency.digits/ru-RU/ru-RU
//Testing gen-int-currency.digits/en-US/en-US
//Testing gen-int-currency.digits/ru-RU/en-US
//Error for gen-int-currency.digits/en-US/en-US in 24 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < Average sentence length: 1238.0.> but was: < Average sentence length: 1,238.0.>
//Testing gen-int-currency.digits/fr-FR/ru-RU
//Error for gen-int-currency.digits/ru-RU/en-US in 33 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < Average sentence length: 1338.0.> but was: < Average sentence length: 1,338.0.>
//Testing gen-int-currency.digits/fr-FR/en-US
//Error for gen-int-currency.digits/ru-RU/ru-RU in 34 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1338,0.> but was: < ������� ����� �����������: 1�338,0.>
//Testing gen-int-currency.digits/zh-CN/ru-RU
//Error for gen-int-currency.digits/fr-FR/ru-RU in 21 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1338,0.> but was: < ������� ����� �����������: 1�338,0.>
//Testing gen-int-currency.digits/zh-CN/en-US
//Error for gen-int-currency.digits/en-US/ru-RU in 50 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1238,0.> but was: < ������� ����� �����������: 1�238,0.>
//Error for gen-int-currency.digits/fr-FR/en-US in 17 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < Average sentence length: 1338.0.> but was: < Average sentence length: 1,338.0.>
//Testing gen-int-currency.digits/ar-MA/ru-RU
//Testing gen-int-currency.digits/ar-MA/en-US
//Error for gen-int-currency.digits/zh-CN/ru-RU in 25 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1238,0.> but was: < ������� ����� �����������: 1�238,0.>
//Testing gen-int-currency.digits/he/ru-RU
//Error for gen-int-currency.digits/zh-CN/en-US in 18 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < Average sentence length: 1238.0.> but was: < Average sentence length: 1,238.0.>
//Testing gen-int-currency.digits/he/en-US
//Error for gen-int-currency.digits/ar-MA/en-US in 24 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < Average sentence length: 1887.0.> but was: < Average sentence length: 1,887.0.>
//Error for gen-int-currency.digits/ar-MA/ru-RU in 26 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1887,0.> but was: < ������� ����� �����������: 1�887,0.>
//Error for gen-int-currency.digits/he/en-US in 19 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < Average sentence length: 1587.0.> but was: < Average sentence length: 1,587.0.>
//Error for gen-int-currency.digits/he/ru-RU in 24 ms: (AssertionFailedError) For gen-int-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1587,0.> but was: < ������� ����� �����������: 1�587,0.>
//Testing gen-int-currency.misc/en-US/ru-RU
//Testing gen-int-currency.misc/en-US/en-US
//Testing gen-int-currency.misc/ru-RU/ru-RU
//Testing gen-int-currency.misc/ru-RU/en-US
//Error for gen-int-currency.misc/en-US/en-US in 25 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < Average sentence length: 1546.0.> but was: < Average sentence length: 1,546.0.>
//Testing gen-int-currency.misc/fr-FR/ru-RU
//Error for gen-int-currency.misc/ru-RU/en-US in 25 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < Average sentence length: 1637.0.> but was: < Average sentence length: 1,637.0.>
//Testing gen-int-currency.misc/fr-FR/en-US
//Error for gen-int-currency.misc/ru-RU/ru-RU in 26 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < ������� ����� �����������: 1637,0.> but was: < ������� ����� �����������: 1�637,0.>
//Testing gen-int-currency.misc/zh-CN/ru-RU
//Error for gen-int-currency.misc/en-US/ru-RU in 26 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < ������� ����� �����������: 1546,0.> but was: < ������� ����� �����������: 1�546,0.>
//Testing gen-int-currency.misc/zh-CN/en-US
//Error for gen-int-currency.misc/fr-FR/en-US in 20 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < Average sentence length: 1626.0.> but was: < Average sentence length: 1,626.0.>
//Testing gen-int-currency.misc/ar-MA/ru-RU
//Error for gen-int-currency.misc/zh-CN/en-US in 19 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < Average sentence length: 1553.0.> but was: < Average sentence length: 1,553.0.>
//Testing gen-int-currency.misc/ar-MA/en-US
//Error for gen-int-currency.misc/zh-CN/ru-RU in 21 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < ������� ����� �����������: 1553,0.> but was: < ������� ����� �����������: 1�553,0.>
//Testing gen-int-currency.misc/he/ru-RU
//Error for gen-int-currency.misc/ar-MA/en-US in 12 ms: (AssertionFailedError) For gen-int-currency.misc: line 29 ==> expected: < Minimal currency: -$961,589.80.> but was: < Minimal currency: -$961589.80.>
//Testing gen-int-currency.misc/he/en-US
//Error for gen-int-currency.misc/ar-MA/ru-RU in 18 ms: (AssertionFailedError) For gen-int-currency.misc: line 29 ==> expected: < ����������� �����: -961�589,80�?.> but was: < ����������� �����: -961589,80�?.>
//Error for gen-int-currency.misc/fr-FR/ru-RU in 45 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < ������� ����� �����������: 1626,0.> but was: < ������� ����� �����������: 1�626,0.>
//Error for gen-int-currency.misc/he/en-US in -153 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < Average sentence length: 1873.0.> but was: < Average sentence length: 1,873.0.>
//Error for gen-int-currency.misc/he/ru-RU in -114 ms: (AssertionFailedError) For gen-int-currency.misc: line 14 ==> expected: < ������� ����� �����������: 1873,0.> but was: < ������� ����� �����������: 1�873,0.>
//Testing gen-float-currency.lines/en-US/ru-RU
//Testing gen-float-currency.lines/en-US/en-US
//Testing gen-float-currency.lines/ru-RU/en-US
//Testing gen-float-currency.lines/ru-RU/ru-RU
//Error for gen-float-currency.lines/en-US/en-US in 21 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < Average sentence length: 1238.0.> but was: < Average sentence length: 1,238.0.>
//Testing gen-float-currency.lines/fr-FR/ru-RU
//Error for gen-float-currency.lines/ru-RU/en-US in 21 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < Average sentence length: 1338.0.> but was: < Average sentence length: 1,338.0.>
//Testing gen-float-currency.lines/fr-FR/en-US
//Error for gen-float-currency.lines/ru-RU/ru-RU in 21 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1338,0.> but was: < ������� ����� �����������: 1�338,0.>
//Testing gen-float-currency.lines/zh-CN/ru-RU
//Error for gen-float-currency.lines/en-US/ru-RU in 22 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1238,0.> but was: < ������� ����� �����������: 1�238,0.>
//Testing gen-float-currency.lines/zh-CN/en-US
//Error for gen-float-currency.lines/fr-FR/en-US in 20 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < Average sentence length: 1338.0.> but was: < Average sentence length: 1,338.0.>
//Testing gen-float-currency.lines/ar-MA/ru-RU
//Error for gen-float-currency.lines/zh-CN/ru-RU in 20 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1238,0.> but was: < ������� ����� �����������: 1�238,0.>
//Testing gen-float-currency.lines/ar-MA/en-US
//Error for gen-float-currency.lines/zh-CN/en-US in 21 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < Average sentence length: 1238.0.> but was: < Average sentence length: 1,238.0.>
//Testing gen-float-currency.lines/he/ru-RU
//Error for gen-float-currency.lines/ar-MA/ru-RU in 21 ms: (AssertionFailedError) For gen-float-currency.lines: line 29 ==> expected: < ����������� �����: -961�589,80�?.> but was: < ����������� �����: -961589,80�?.>
//Testing gen-float-currency.lines/he/en-US
//Error for gen-float-currency.lines/ar-MA/en-US in 21 ms: (AssertionFailedError) For gen-float-currency.lines: line 29 ==> expected: < Minimal currency: -$961,589.80.> but was: < Minimal currency: -$961589.80.>
//Error for gen-float-currency.lines/he/ru-RU in 21 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1587,0.> but was: < ������� ����� �����������: 1�587,0.>
//Error for gen-float-currency.lines/fr-FR/ru-RU in 48 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < ������� ����� �����������: 1338,0.> but was: < ������� ����� �����������: 1�338,0.>
//Error for gen-float-currency.lines/he/en-US in 16 ms: (AssertionFailedError) For gen-float-currency.lines: line 14 ==> expected: < Average sentence length: 1587.0.> but was: < Average sentence length: 1,587.0.>
//Testing gen-float-currency.spaces/en-US/ru-RU
//Testing gen-float-currency.spaces/en-US/en-US
//Testing gen-float-currency.spaces/ru-RU/en-US
//Testing gen-float-currency.spaces/ru-RU/ru-RU
//Error for gen-float-currency.spaces/en-US/en-US in 24 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < Average sentence length: 1374.0.> but was: < Average sentence length: 1,374.0.>
//Testing gen-float-currency.spaces/fr-FR/ru-RU
//Error for gen-float-currency.spaces/en-US/ru-RU in 25 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1374,0.> but was: < ������� ����� �����������: 1�374,0.>
//Testing gen-float-currency.spaces/fr-FR/en-US
//Error for gen-float-currency.spaces/ru-RU/ru-RU in 24 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1516,0.> but was: < ������� ����� �����������: 1�516,0.>
//Testing gen-float-currency.spaces/zh-CN/ru-RU
//Error for gen-float-currency.spaces/ru-RU/en-US in 25 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < Average sentence length: 1516.0.> but was: < Average sentence length: 1,516.0.>
//Testing gen-float-currency.spaces/zh-CN/en-US
//Error for gen-float-currency.spaces/zh-CN/ru-RU in 19 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1434,0.> but was: < ������� ����� �����������: 1�434,0.>
//Testing gen-float-currency.spaces/ar-MA/ru-RU
//Error for gen-float-currency.spaces/zh-CN/en-US in 20 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < Average sentence length: 1434.0.> but was: < Average sentence length: 1,434.0.>
//Testing gen-float-currency.spaces/ar-MA/en-US
//Error for gen-float-currency.spaces/fr-FR/en-US in 30 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < Average sentence length: 1497.0.> but was: < Average sentence length: 1,497.0.>
//Testing gen-float-currency.spaces/he/ru-RU
//Error for gen-float-currency.spaces/ar-MA/ru-RU in 19 ms: (AssertionFailedError) For gen-float-currency.spaces: line 29 ==> expected: < ����������� �����: -961�589,80�?.> but was: < ����������� �����: -961589,80�?.>
//Testing gen-float-currency.spaces/he/en-US
//Error for gen-float-currency.spaces/ar-MA/en-US in 20 ms: (AssertionFailedError) For gen-float-currency.spaces: line 29 ==> expected: < Minimal currency: -$961,589.80.> but was: < Minimal currency: -$961589.80.>
//Error for gen-float-currency.spaces/fr-FR/ru-RU in 48 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1497,0.> but was: < ������� ����� �����������: 1�497,0.>
//Error for gen-float-currency.spaces/he/en-US in 19 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < Average sentence length: 1742.0.> but was: < Average sentence length: 1,742.0.>
//Error for gen-float-currency.spaces/he/ru-RU in 33 ms: (AssertionFailedError) For gen-float-currency.spaces: line 14 ==> expected: < ������� ����� �����������: 1742,0.> but was: < ������� ����� �����������: 1�742,0.>
//Testing gen-float-currency.words/en-US/ru-RU
//Testing gen-float-currency.words/en-US/en-US
//Testing gen-float-currency.words/ru-RU/ru-RU
//Testing gen-float-currency.words/ru-RU/en-US
//Error for gen-float-currency.words/en-US/ru-RU in 24 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < ������� ����� �����������: 1689,0.> but was: < ������� ����� �����������: 1�689,0.>
//Testing gen-float-currency.words/fr-FR/ru-RU
//Error for gen-float-currency.words/ru-RU/ru-RU in 24 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < ������� ����� �����������: 1764,0.> but was: < ������� ����� �����������: 1�764,0.>
//Testing gen-float-currency.words/fr-FR/en-US
//Error for gen-float-currency.words/ru-RU/en-US in 25 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < Average sentence length: 1764.0.> but was: < Average sentence length: 1,764.0.>
//Testing gen-float-currency.words/zh-CN/ru-RU
//Error for gen-float-currency.words/en-US/en-US in 27 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < Average sentence length: 1689.0.> but was: < Average sentence length: 1,689.0.>
//Testing gen-float-currency.words/zh-CN/en-US
//Error for gen-float-currency.words/zh-CN/en-US in 17 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < Average sentence length: 1663.0.> but was: < Average sentence length: 1,663.0.>
//Testing gen-float-currency.words/ar-MA/ru-RU
//Error for gen-float-currency.words/fr-FR/en-US in 20 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < Average sentence length: 1795.0.> but was: < Average sentence length: 1,795.0.>
//Testing gen-float-currency.words/ar-MA/en-US
//Error for gen-float-currency.words/zh-CN/ru-RU in 20 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < ������� ����� �����������: 1663,0.> but was: < ������� ����� �����������: 1�663,0.>
//Testing gen-float-currency.words/he/ru-RU
//Error for gen-float-currency.words/ar-MA/en-US in 13 ms: (AssertionFailedError) For gen-float-currency.words: line 29 ==> expected: < Minimal currency: -$961,589.80.> but was: < Minimal currency: -$961589.80.>
//Testing gen-float-currency.words/he/en-US
//Error for gen-float-currency.words/ar-MA/ru-RU in 14 ms: (AssertionFailedError) For gen-float-currency.words: line 29 ==> expected: < ����������� �����: -961�589,80�?.> but was: < ����������� �����: -961589,80�?.>
//Error for gen-float-currency.words/he/en-US in 19 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < Average sentence length: 2040.0.> but was: < Average sentence length: 2,040.0.>
//Error for gen-float-currency.words/he/ru-RU in 49 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < ������� ����� �����������: 2040,0.> but was: < ������� ����� �����������: 2�040,0.>
//Error for gen-float-currency.words/fr-FR/ru-RU in 72 ms: (AssertionFailedError) For gen-float-currency.words: line 14 ==> expected: < ������� ����� �����������: 1795,0.> but was: < ������� ����� �����������: 1�795,0.>
//Testing gen-float-currency.digits/en-US/ru-RU
//Testing gen-float-currency.digits/en-US/en-US
//Testing gen-float-currency.digits/ru-RU/ru-RU
//Testing gen-float-currency.digits/ru-RU/en-US
//Error for gen-float-currency.digits/en-US/ru-RU in 22 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1238,0.> but was: < ������� ����� �����������: 1�238,0.>
//Testing gen-float-currency.digits/fr-FR/ru-RU
//Error for gen-float-currency.digits/en-US/en-US in 22 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < Average sentence length: 1238.0.> but was: < Average sentence length: 1,238.0.>
//Testing gen-float-currency.digits/fr-FR/en-US
//Error for gen-float-currency.digits/ru-RU/ru-RU in 22 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1338,0.> but was: < ������� ����� �����������: 1�338,0.>
//Testing gen-float-currency.digits/zh-CN/ru-RU
//Error for gen-float-currency.digits/ru-RU/en-US in 23 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < Average sentence length: 1338.0.> but was: < Average sentence length: 1,338.0.>
//Testing gen-float-currency.digits/zh-CN/en-US
//Error for gen-float-currency.digits/fr-FR/en-US in 18 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < Average sentence length: 1338.0.> but was: < Average sentence length: 1,338.0.>
//Testing gen-float-currency.digits/ar-MA/ru-RU
//Error for gen-float-currency.digits/zh-CN/ru-RU in 18 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1238,0.> but was: < ������� ����� �����������: 1�238,0.>
//Testing gen-float-currency.digits/ar-MA/en-US
//Error for gen-float-currency.digits/zh-CN/en-US in 17 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < Average sentence length: 1238.0.> but was: < Average sentence length: 1,238.0.>
//Testing gen-float-currency.digits/he/ru-RU
//Error for gen-float-currency.digits/he/ru-RU in 21 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1587,0.> but was: < ������� ����� �����������: 1�587,0.>
//Testing gen-float-currency.digits/he/en-US
//Error for gen-float-currency.digits/ar-MA/ru-RU in 21 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1887,0.> but was: < ������� ����� �����������: 1�887,0.>
//Error for gen-float-currency.digits/ar-MA/en-US in 24 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < Average sentence length: 1887.0.> but was: < Average sentence length: 1,887.0.>
//Error for gen-float-currency.digits/fr-FR/ru-RU in 52 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < ������� ����� �����������: 1338,0.> but was: < ������� ����� �����������: 1�338,0.>
//Error for gen-float-currency.digits/he/en-US in 17 ms: (AssertionFailedError) For gen-float-currency.digits: line 14 ==> expected: < Average sentence length: 1587.0.> but was: < Average sentence length: 1,587.0.>
//Testing gen-float-currency.misc/en-US/ru-RU
//Testing gen-float-currency.misc/en-US/en-US
//Testing gen-float-currency.misc/ru-RU/ru-RU
//Testing gen-float-currency.misc/ru-RU/en-US
//Error for gen-float-currency.misc/ru-RU/ru-RU in 22 ms: (AssertionFailedError) For gen-float-currency.misc: line 14 ==> expected: < ������� ����� �����������: 1665,0.> but was: < ������� ����� �����������: 1�665,0.>
//Testing gen-float-currency.misc/fr-FR/ru-RU
//Error for gen-float-currency.misc/ru-RU/en-US in 22 ms: (AssertionFailedError) For gen-float-currency.misc: line 14 ==> expected: < Average sentence length: 1665.0.> but was: < Average sentence length: 1,665.0.>
//Testing gen-float-currency.misc/fr-FR/en-US
//Error for gen-float-currency.misc/en-US/en-US in 29 ms: (AssertionFailedError) For gen-float-currency.misc: line 14 ==> expected: < Average sentence length: 1545.0.> but was: < Average sentence length: 1,545.0.>
//Testing gen-float-currency.misc/zh-CN/ru-RU