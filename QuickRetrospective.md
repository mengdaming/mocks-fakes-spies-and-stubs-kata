# Quick Retrospective

## Fakes, Mocks, ...

| Team             | Date       |
|------------------|------------|
| Fakers & Mockers | 10/06/2022 |

## DO
_`What did we do?`_

- Brainstormed on this kata
- Implemented all kinds of tests
- Used mockito
- Broke the tests in different ways (introducing bugs, changing implementations)

## LEARN
_`What did we learn?`_

- The kata helps hands on fakes, stubs, spies and mocks
- (Damien) playing with Mockito
- With Mockito, it's difficult to differentiate spies from mocks
- Depending on what you want to test, some test doubles don't make sense
- The description from [The Little Mocker](https://blog.cleancoder.com/uncle-bob/2014/05/14/TheLittleMocker.html) are much clearer than the kata slides

## PUZZLE
_`What still puzzles us?`_

- Not obvious to demonstrate what kind of test double gets broken by what kind of change
- There are mockitos all over the place, which makes it hard to learn different types of test doubles
- Kata duration may vary a lot depending on participants' fluency with Mockito
- Do we need to reorganize a bit the classes to clearly separate fakes on one side, and mocks and friends on the other side
- "Mockito Spies" e.g. Proxies are not covered in this kata

## DECIDE
_`How can we apply this in the future?`_

- We try this kata with Morrigan, squashing mock&friends test cases altogether
- We should Share the little mocker to the team a few days before
- Be more prescriptive about the change to introduce
- Run the pros & cons & fixes activity at the end

