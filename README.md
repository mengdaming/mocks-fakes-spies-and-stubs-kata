# Mocks, Fakes, Spies and Stubs Kata

> This is an adaptation of the original kata available at
> [github.com/aleixmorgadas/mocks-fakes-spies-and-stubs-kata](https://github.com/aleixmorgadas/mocks-fakes-spies-and-stubs-kata)

## Recommended Reading

- Blog post [The Little Mocker](https://blog.cleancoder.com/uncle-bob/2014/05/14/TheLittleMocker.html) by Robert C. Martin a.k.a. Uncle Bob

## Setup Environment

1. Clone the repository
    ```shell
    git clone https://stash.murex.com/scm/xps/mocks-fakes-spies-and-stubs-kata.git
   ```
2. Check that all tests are green
    ```shell
    ./gradlew test
    ```

## How is the kata structured?

- `kata.domain.film`: Test-Doubles examples made with Mockito and hand-made examples.
- `kata.domain.user`: Support domain to represent the UserId
- `kata.domain.rate`: Actual kata
- `kata.support`: InMemoryRepositories

## Running the kata

### 1 - Implement `RateService` tests using different kinds of test doubles

Check the tests in [kata.domain.rate](./src/test/java/kata/domain/rate).

You will see [RateServiceTest_Fake](./src/test/java/kata/domain/rate/RateServiceTest_Fake.java)
and [RateServiceTest_Mock_Stub_Spy](./src/test/java/kata/domain/rate/RateServiceTest_Mock_Stub_Spy.java).
Implement each Test following the named pattern. 

__Info:__ Remember that using two different Test Double types in the same test is allowed.  

### 2 - Alter the behaviour of `RateService` and observe impacts on the tests

For each of the following cases, **observe which tests turn red and which remain green**.

1. Introduce some bugs
2. Refactor by adding RateRepository.save(rate)
3. Refactor by adding RateRepository.findByUser(userId) for optimisation
4. Decreased the notification threshold by one

### 3 - Replace `FilmService` mock with a fake

In `RateService` test setups, use `FilmRepositoryInMemory` instead of mocking `FilmService`.
What do you observe?

### 4 - Test Doubles Pros, Cons and Fixes

Fill a table listing the pros, cons and fixes for each type of Test Double

| Test Double Family  | Pros | Cons | Fixes |
|---------------------|------|------|-------|
| Mocks, Spies, Stubs |      |      |       |
| Fakes               |      |      |       |

## Session Quick Retrospective

You can fill it from [here](QuickRetrospective.md)

## Support Docs

- [Business Definitions](docs/BusinessDefinitions.md)
- [Kata Slides](https://docs.google.com/presentation/d/1RTIjilK8zIiKfilBqD8x9UavBJFn089ORiLlBBgjidg)
