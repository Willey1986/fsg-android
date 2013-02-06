-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 31. Jan 2013 um 11:50
-- Server Version: 5.5.29
-- PHP-Version: 5.3.10-1ubuntu3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `fsgbackend`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `client_identities`
--

CREATE TABLE IF NOT EXISTS `client_identities` (
  `dtn_address` varchar(80) NOT NULL DEFAULT 'dtn://',
  `alias` varchar(80) NOT NULL DEFAULT '-'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dtnd`
--

CREATE TABLE IF NOT EXISTS `dtnd` (
  `is_running` tinyint(1) NOT NULL DEFAULT '0',
  `pid` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `dtnd`
--

INSERT INTO `dtnd` (`is_running`, `pid`) VALUES
(0, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dtntrigger`
--

CREATE TABLE IF NOT EXISTS `dtntrigger` (
  `is_running` tinyint(1) NOT NULL DEFAULT '0',
  `pid` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `dtntrigger`
--

INSERT INTO `dtntrigger` (`is_running`, `pid`) VALUES
(0, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dtntrigger_log`
--

CREATE TABLE IF NOT EXISTS `dtntrigger_log` (
  `time_stamp` varchar(20) NOT NULL,
  `msg` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `versions_clients`
--

CREATE TABLE IF NOT EXISTS `versions_clients` (
  `dtn_address` varchar(80) NOT NULL DEFAULT 'dtn://',
  `drivers` int(11) NOT NULL DEFAULT '0',
  `teams` int(11) NOT NULL DEFAULT '0',
  `blacklist_devices` int(11) NOT NULL DEFAULT '0',
  `blacklist_tags` int(11) NOT NULL DEFAULT '0',
  `driver_pics` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `versions_server`
--

CREATE TABLE IF NOT EXISTS `versions_server` (
  `drivers` int(11) NOT NULL DEFAULT '0',
  `teams` int(11) NOT NULL DEFAULT '0',
  `blacklist_devices` int(11) NOT NULL DEFAULT '0',
  `blacklist_tags` int(11) NOT NULL DEFAULT '0',
  `driver_pics` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `versions_server`
--

INSERT INTO `versions_server` (`drivers`, `teams`, `blacklist_devices`, `blacklist_tags`, `driver_pics`) VALUES
(1, 1, 1, 1, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
