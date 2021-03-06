# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v2.0.1] - 2020-01-03
### Fixed
- Error `Failed to read player statistic: Label cannot be null.` on metrics `player_online` and `player_statistic`. 
  If the server doesn't return a name for a player, the player's UID is used as label instead. (https://github.com/sladkoff/minecraft-prometheus-exporter/issues/17)

### Added
- Labels `uid`/`player_uid` for metrics `player_online` and `player_statistic` in addition to the player name.

## [v2.0.0] - 2019-12-29
### Changed
- :warning: [Breaking] Metric `mc_players_total` no longer has a `state` label. It exports only the number of unique players now. 
  Use `mc_players_online_total` for online player count.

### Added
- Metrics can be enabled individually via config `enable_metrics`. Since the format of the config.yml has changed it is advised to delete your existing file to 
  generate a new clean one. 

## [v1.3.0] - 2019-05-28
### Added
- This changelog
- Add `host` config property
### Changed
- Listen on `localhost` interface by default

## [v1.2.0] - 2018-11-05
### Added
- Experimental player statistics

## [v1.1.0] - 2017-03-18
### Added
- Export tickrate (TPS)

## [v1.0.1] - 2017-02-19
### Fixed
- Concurrency issues
### Changed
- Return 404 on unsupported request URI's

## [v1.0.0] - 2017-02-14
### Added 
- Export JVM memory usage


## v0.1.0 - 2017-02-09
### Added
- Initial exporter

[v2.0.1]: https://github.com/sladkoff/minecraft-prometheus-exporter/compare/v2.0.0...v2.0.1
[v2.0.0]: https://github.com/sladkoff/minecraft-prometheus-exporter/compare/v1.3.0...v2.0.0
[v1.3.0]: https://github.com/sladkoff/minecraft-prometheus-exporter/compare/v1.2.0...v1.3.0
[v1.2.0]: https://github.com/sladkoff/minecraft-prometheus-exporter/compare/v1.1.0...v1.2.0
[v1.1.0]: https://github.com/sladkoff/minecraft-prometheus-exporter/compare/v1.0.1...v1.1.0
[v1.0.1]: https://github.com/sladkoff/minecraft-prometheus-exporter/compare/v1.0.0...v1.0.1
[v1.0.0]: https://github.com/sladkoff/minecraft-prometheus-exporter/compare/v0.1.0...v1.0.0
