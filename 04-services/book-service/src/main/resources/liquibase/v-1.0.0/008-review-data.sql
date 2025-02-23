--liquibase formatted sql

--changeset ysavchen:008.01 runOnChange:false splitStatements:false runInTransaction:false
INSERT INTO reviews (id, title, review_text, author, rating, book_fk)
VALUES
  (
    '0190bd7f-65f1-70ed-869d-0391d3d898b3',
    'Great',
    'VmVyeSBoYXBweS4gTG92ZWx5IGJvb2su',
    'Emma Stevenson',
    5.0,
    get_book_id('Unforgettable Journeys: Slow Down and See the World')
  ),
  (
    '0190bd7f-b143-7bcb-9ac3-689ca0358ae2',
    'gorgeous',
    'YWJzb2x1dGVseSBmYW50YXN0aWMgYm9vay4gZ29vZCBiYWxhbmNlIG9mIGJlYXV0aWZ1bCBwaWN0dXJlcyBhbmQgdml2aWQgdGV4dC4gSSBib3VnaHQgdGhpcyBmb3Igc29tZW9uZSBlbHNlIGFuZCBhbG1vc3Qga2VwdCBpdCBmb3IgbXlzZWxmLg==',
    'Patrick',
    5.0,
    get_book_id('Unforgettable Journeys: Slow Down and See the World')
  ),
  (
    '0190bd7f-ec03-7511-8788-58c03fd596f8',
    'for people who love traveling, but couldn''t travel this year',
    'VGhpcyBpcyBhIGdvb2QgZ2lmdCBib29rIGZvciBwZW9wbGUgd2hvIGxvdmUgdHJhdmVsaW5nLCBidXQgY291bGRuJ3QgdHJhdmVsIHRoaXMgeWVhciBkdWUgdG8gdGhlIHBhbmRlbWljLiBESyBib29rcyBhcmUgZ3JlYXQgZm9yIHNob3dpbmcgeW91IGRlc3RpbmF0aW9ucyB0aHJvdWdoIHBob3RvcyBhbmQgSSB0aGluayB0aGlzIGtpbmQgb2YgcHJlc2VudGF0aW9uIHN0aWxsIHdvcmtzIGJldHRlciB0aGFuIG9ubGluZSwgZXNwZWNpYWxseSBpZiB5b3VyIGludGVybmV0IGlzIHRvbyBzbG93IHRvIGxldCB5b3UgY29tZm9ydGFibHkgc2Nyb2xsIHRocm91Z2ggYSBjb2xsZWN0aW9uIG9mIGxhcmdlIGNvbG9yZnVsIHBob3Rvcy4gVGhlIGJvb2sgc2hvd3MgeW91IGludGVyZXN0aW5nIHBsYWNlcyB0byB2aXNpdCBhcm91bmQgdGhlIHdvcmxkIHdpdGggcGhvdG9zIGFuZCBhIGJyaWVmIHRleHQgaW50cm9kdWN0aW9uIHRvIHRoZSBhcmVhLiBNYW55IGFyZSBvZmYgdGhlIGJlYXRlbiB0aGUgbWFpbiB0b3VyaXN0IHRyYWNrLCBidXQgbm90IHRvbyBoYXJkIHRvIGdldCB0byBpZiB5b3Ugd2FudCB0byBwdXQgaW4gdGhlIGVmZm9ydC4gVGhleSBjb3ZlciBhIGxvdCBvZiBkaWZmZXJlbnQgZGVzdGluYXRpb25zIHdpdGhvdXQgdG9vIG11Y2ggZGV0YWlscywgYnV0IHlvdSBjYW4gZWFzaWx5IGxvb2sgdGhlbSB1cCBvbiB0aGUgaW50ZXJuZXQgaWYgeW91IGxpa2VkIHRoZSBpbnRyb2R1Y3Rpb24uIFdobyBrbm93cyB3aGVuIHRoZSBwYW5kZW1pYyB3aWxsIGJlIG92ZXIsIGJ1dCBkb2Vzbid0IGh1cnQgdG8gc3RhcnQgcGxhbm5pbmcgeW91ciBuZXh0IHRyaXBzIG5vdy4=',
    'l2',
    4.0,
    get_book_id('Unforgettable Journeys: Slow Down and See the World')
  ),
  (
    '0190bd80-1e84-722a-814e-be7c45ea06c3',
    'Beautiful Book',
    'R3JlYXQgZm9yIGNvZmZlZSB0YWJsZSByZWFkaW5nIQ==',
    'Mangu04',
     5.0,
     get_book_id('Unforgettable Journeys: Slow Down and See the World')
  ),
  (
    '0190bd80-571e-723d-935e-201cbf56b450',
    'Stunning',
    'VGhpcyBib29rIGlzIHN1Y2ggYSBnZW0uIEJlYXV0aWZ1bCBwaWN0dXJlcywgZ3JlYXQgdHJpcHMu',
    'pittiSA',
    5.0,
    get_book_id('Unforgettable Journeys: Slow Down and See the World')
  ),
  (
    '0190bd80-7fb0-7e39-8647-4ea0f67818b6',
    'lovely and inspiring- wish photos were bigger!',
    'VGhpcyBpcyBhIGJlYXV0aWZ1bCBib29rLSBJJ20gdXNpbmcgaXQgYXMgYm90aCBwaG90byByZWZlcmVuY2UgZm9yIG15IHBlcnNvbmFsIHNrZXRjaGJvb2sgYW5kIHRvIGxlYXJuIG1vcmUgYWJvdXQgdGhlIHdvcmxkIEkgbGl2ZSBpbiBhbmQgYmVhdXRpZnVsIHBsYWNlcyBpbiBpdC4gTXkgb25seSBjb21wbGFpbnRzIGlzIEkgd2lzaCB0aGUgcGhvdG9zIHdlcmUgbGFyZ2VyICh0aGV5IGFyZSBiZWF1dGlmdWwsIGJ1dCBvZnRlbiAiYXJ0aWNsZSIgc2l6ZSBpbnN0ZWFkIG9mIHRha2luZyBhIGZ1bGwgcGFnZSkgYW5kIHRoZSBib29rIGhhZCBiZWVuIHByaW50ZWQgb24gcGFwZXIgdGhhdCBoYWQgbW9yZSBzaGVlbiwgdG8gbWFrZSB0aGUgY29sb3IgaW4gdGhlIHBob3RvcyAod2hpY2ggYXJlLCBhZ2FpbiwgZ29yZ2VvdXMpIHJlYWxseSBwb3AuIFRoaXMgaXMgYSBib29rIHlvdSBhY3R1YWxseSBkaXAgaW50byBhbmQgcmVhZCBmb3IgYSB3aGlsZS0gbm90IGp1c3QgYSBjb2ZmZWUgdGFibGUgcGhvdG8gYm9vay4gVmVyeSBnZW5lcm91c2x5IHNpemVkLg==',
    'C.M. & T.M.',
    4.0,
    get_book_id('Unforgettable Journeys: Slow Down and See the World')
  ),
  (
    '0190bd80-c517-7990-82fe-a5e077c9c8a1',
    'Beautiful gift',
    'SSBnYXZlIHRoaXMgdG8gbXkgdHJhdmVsIGFnZW50IGZyaWVuZCBhbmQgYWx0aG91Z2ggc2hlIGhhcyBiZWVuIHRvIHF1aXRlIGEgZmV3IHBsYWNlcyB0aGlzIHRvbWUgaXMgYSBsb3ZlbHkgcmVtaW5kZXIgb2YgcGxhY2VzIHZpc2l0ZWQgb3IgYSBidWNrZXQgbGlzdCBmb3IgcGxhY2VzIG9uZSBtdXN0IHZpc2l0Lg==',
    'Carole F. Froelich',
    4.0,
    get_book_id('Destinations of a Lifetime: 225 of the World''s Most Amazing Places')
  ),
  (
    '0190bd81-066c-74d5-91e5-7120e918e270',
    'Beautiful National Geographic photos',
    'UGhvdG9ncmFwaHkgaXMgdW5iZWxpZXZhYmx5IGJlYXV0aWZ1bCwgYXMgdXN1YWwgZm9yIGZvciBOYXRpb25hbCBHZW9ncmFwaGljIGJvb2tzLgpFeGNlbGxlbnQgZGlzY291bnQgcHJpY2UuCkJpZyBhbmQgaGVhdnkgd291bGQgYmUgaWRlYWwgYXMgYSBjb2ZmZWUgdGFibGUgYm9vayBldmVuIHN1aXRhYmxlIGZvciBhIHByb2Zlc3Npb25hbCB3YWl0aW5nIHJvb20=',
    'Ann',
    5.0,
    get_book_id('Destinations of a Lifetime: 225 of the World''s Most Amazing Places')
  ),
  (
    '0190bd81-3cb4-7408-874a-047a95212ff5',
    'My coffee table book',
    'R3JlYXQgY29mZmVlIHRhYmxlIGJvb2ssIGl04oCZcyBiZWVuIG9uIG15IGNvZmZlZSB0YWJsZSBmb3IgY291cGxlIHllYXJzIG5vdy4gSSByZWFkLCBsZWFybiBhbmQgY2hlY2sgbWFyayBhZnRlciBwbGFjZXMgSSB2aXNpdC4gSeKAmW0gdmlzdWFsIHBlcnNvbiBzbyB0aGlzIGJvb2sgaGFzIGdyZWF0IGJpZyBjb2xvdXJmdWwgcGljdHVyZXMuIEkgbmVlZCB0byBnZXQgb25lIG1vcmUgdXBkYXRlZCBvbmUgYnV0IHRoaXMgb25lIEkgbG92ZSB0b28gbXVjaC4=',
    'Donna',
    5.0,
    get_book_id('Destinations of a Lifetime: 225 of the World''s Most Amazing Places')
  ),
  (
    '0190bd81-5236-7223-a7df-a086dd3597d7',
    'Stunningly Beautiful Photos',
    'VHJhdmVsIHdoaWxlIG5vdCBsZWF2aW5nIHlvdXIgaG91c2UhIEdvcmdlb3VzIHBob3RvcyBvZiBhbGwga2luZHMgb2YgcGxhY2VzIHdpdGggZGVzY3JpcHRpb25zISBBIGdyZWF0IGNvZmZlZS10YWJsZSBib29rIQ==',
    'Monika O.',
    5.0,
    get_book_id('Destinations of a Lifetime: 225 of the World''s Most Amazing Places')
  );