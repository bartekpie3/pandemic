package com.example.common;

public sealed interface Result<S, F> {

  boolean isSuccess();

  S value();

  F error();

  record Success<S, F>(S value) implements Result<S, F> {

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public F error() {
      return null;
    }
  }

  record Failure<S, F>(F error) implements Result<S, F> {
    @Override
    public boolean isSuccess() {
      return false;
    }

    @Override
    public S value() {
      return null;
    }
  }
}
