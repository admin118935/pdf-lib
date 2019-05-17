import CharCodes from 'src/core/CharCodes';

export const EndstreamEolChars = [CharCodes.Newline, CharCodes.CarriageReturn];

export const Keywords = {
  header: [
    CharCodes.Percent,
    CharCodes.P,
    CharCodes.D,
    CharCodes.F,
    CharCodes.Dash,
  ],
  eof: [
    CharCodes.Percent,
    CharCodes.Percent,
    CharCodes.E,
    CharCodes.O,
    CharCodes.F,
  ],
  obj: [CharCodes.o, CharCodes.b, CharCodes.j],
  endobj: [
    CharCodes.e,
    CharCodes.n,
    CharCodes.d,
    CharCodes.o,
    CharCodes.b,
    CharCodes.j,
  ],
  xref: [CharCodes.x, CharCodes.r, CharCodes.e, CharCodes.f],
  trailer: [
    CharCodes.t,
    CharCodes.r,
    CharCodes.a,
    CharCodes.i,
    CharCodes.l,
    CharCodes.e,
    CharCodes.r,
  ],
  startxref: [
    CharCodes.s,
    CharCodes.t,
    CharCodes.a,
    CharCodes.r,
    CharCodes.t,
    CharCodes.x,
    CharCodes.r,
    CharCodes.e,
    CharCodes.f,
  ],
  true: [CharCodes.t, CharCodes.r, CharCodes.u, CharCodes.e],
  false: [CharCodes.f, CharCodes.a, CharCodes.l, CharCodes.s, CharCodes.e],
  null: [CharCodes.n, CharCodes.u, CharCodes.l, CharCodes.l],
  stream: [
    CharCodes.s,
    CharCodes.t,
    CharCodes.r,
    CharCodes.e,
    CharCodes.a,
    CharCodes.m,
  ],
  endstream: [
    CharCodes.e,
    CharCodes.n,
    CharCodes.d,
    CharCodes.s,
    CharCodes.t,
    CharCodes.r,
    CharCodes.e,
    CharCodes.a,
    CharCodes.m,
  ],
};