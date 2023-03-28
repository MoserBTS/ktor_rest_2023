-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 28 mars 2023 à 22:16
-- Version du serveur : 10.4.25-MariaDB
-- Version de PHP : 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `mydb`
--

-- --------------------------------------------------------

--
-- Structure de la table `administrateurs`
--

CREATE TABLE `administrateurs` (
  `id` int(11) NOT NULL,
  `nom` varchar(45) DEFAULT NULL,
  `prenom` varchar(45) CHARACTER SET armscii8 DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `login` varchar(45) DEFAULT NULL,
  `motdepasse` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `administrateurs`
--

INSERT INTO `administrateurs` (`id`, `nom`, `prenom`, `email`, `login`, `motdepasse`) VALUES
(1, 'Moser', 'Micha?l', 'mm@gmail.com', 'admin', 'admin');

-- --------------------------------------------------------

--
-- Structure de la table `benevoles`
--

CREATE TABLE `benevoles` (
  `id` int(11) NOT NULL,
  `nom` varchar(45) DEFAULT NULL,
  `prenom` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `login` varchar(45) DEFAULT NULL,
  `motdepasse` varchar(45) DEFAULT NULL,
  `age` varchar(45) DEFAULT NULL,
  `autorisation` varchar(45) DEFAULT NULL,
  `festivals_id_festival` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `benevoles`
--

INSERT INTO `benevoles` (`id`, `nom`, `prenom`, `email`, `login`, `motdepasse`, `age`, `autorisation`, `festivals_id_festival`) VALUES
(1, 'Charriere', 'Fabio', 'fabiocharierre@gmail.com', 'fab', 'Llskjdfn4#', '01/01/2020', '1', 3),
(2, 'Boche', 'julien', 'bb@gmail.com', 'bb', 'Mich@3l07', '01/01/2020', '3', 1),
(3, 'Nabet', 'Louis', 'louis.nabet@yahoo.fr', 'draxil', 'Mich@3l07', '01/01/2020', '0', 1);

-- --------------------------------------------------------

--
-- Structure de la table `benevoles_joint_stands`
--

CREATE TABLE `benevoles_joint_stands` (
  `id_benevole_joint_stand` int(11) NOT NULL,
  `benevoles_id_benevoles` int(11) NOT NULL,
  `stands_id_stands` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `dates_festivals`
--

CREATE TABLE `dates_festivals` (
  `id` int(11) NOT NULL,
  `festivals_id_festival` int(11) NOT NULL,
  `dates` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `dates_festivals`
--

INSERT INTO `dates_festivals` (`id`, `festivals_id_festival`, `dates`) VALUES
(10, 5, '20/08/2023'),
(22, 1, '29/06/2023'),
(23, 2, '17/03/2023'),
(24, 4, '16/03/2023'),
(25, 3, '14/07/1971');

-- --------------------------------------------------------

--
-- Structure de la table `dates_joint_benevoles`
--

CREATE TABLE `dates_joint_benevoles` (
  `id_date_joint_benevole` int(11) NOT NULL,
  `benevole_id_benevole` int(11) NOT NULL,
  `dates_festivals_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `dates_joint_benevoles`
--

INSERT INTO `dates_joint_benevoles` (`id_date_joint_benevole`, `benevole_id_benevole`, `dates_festivals_id`) VALUES
(18, 3, 22),
(19, 2, 22),
(25, 1, 25);

-- --------------------------------------------------------

--
-- Structure de la table `festivals`
--

CREATE TABLE `festivals` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `festivals`
--

INSERT INTO `festivals` (`id`, `nom`) VALUES
(1, 'id-1'),
(2, 'id-2'),
(3, 'id-3'),
(4, 'id-4'),
(5, 'id-5');

-- --------------------------------------------------------

--
-- Structure de la table `stands`
--

CREATE TABLE `stands` (
  `id` int(11) NOT NULL,
  `nom` varchar(45) DEFAULT NULL,
  `festivals_id_festival` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `stands`
--

INSERT INTO `stands` (`id`, `nom`, `festivals_id_festival`) VALUES
(1, 'stand A', 3),
(2, 'Stand B', 1),
(3, 'Stand C', 1),
(4, 'Stand D', 1),
(5, 'Stand E', 1),
(6, 'Stand F', 1),
(7, 'Stand G', 2),
(8, 'Stand H', 3),
(9, 'Stand I', 3),
(10, 'Stand J', 1),
(11, 'Stand K', 2),
(12, 'Stand L', 2),
(13, 'Stand M', 2),
(14, 'Stand N', 2),
(15, 'Stand O', 3);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `administrateurs`
--
ALTER TABLE `administrateurs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login_UNIQUE` (`login`);

--
-- Index pour la table `benevoles`
--
ALTER TABLE `benevoles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login_UNIQUE` (`login`),
  ADD UNIQUE KEY `email_benevole_UNIQUE` (`email`),
  ADD KEY `festivals_is_festival` (`festivals_id_festival`);

--
-- Index pour la table `benevoles_joint_stands`
--
ALTER TABLE `benevoles_joint_stands`
  ADD PRIMARY KEY (`id_benevole_joint_stand`),
  ADD KEY `fk_benevoles_has_stands_stands1_idx` (`stands_id_stands`),
  ADD KEY `fk_benevoles_has_stands_benevoles1_idx` (`benevoles_id_benevoles`);

--
-- Index pour la table `dates_festivals`
--
ALTER TABLE `dates_festivals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_dates_festivals_festivals1_idx` (`festivals_id_festival`),
  ADD KEY `fk_dates_festivals_festivals1` (`festivals_id_festival`);

--
-- Index pour la table `dates_joint_benevoles`
--
ALTER TABLE `dates_joint_benevoles`
  ADD PRIMARY KEY (`id_date_joint_benevole`),
  ADD KEY `fk_dates_benevoles_has_benevoles_benevoles1_idx` (`benevole_id_benevole`),
  ADD KEY `dates_festivals_id` (`dates_festivals_id`);

--
-- Index pour la table `festivals`
--
ALTER TABLE `festivals`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `stands`
--
ALTER TABLE `stands`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_stands_festivals1_idx` (`festivals_id_festival`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `administrateurs`
--
ALTER TABLE `administrateurs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `benevoles`
--
ALTER TABLE `benevoles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `benevoles_joint_stands`
--
ALTER TABLE `benevoles_joint_stands`
  MODIFY `id_benevole_joint_stand` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `dates_festivals`
--
ALTER TABLE `dates_festivals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT pour la table `dates_joint_benevoles`
--
ALTER TABLE `dates_joint_benevoles`
  MODIFY `id_date_joint_benevole` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT pour la table `festivals`
--
ALTER TABLE `festivals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `stands`
--
ALTER TABLE `stands`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `benevoles`
--
ALTER TABLE `benevoles`
  ADD CONSTRAINT `benevoles_ibfk_1` FOREIGN KEY (`festivals_id_festival`) REFERENCES `festivals` (`id`) ON UPDATE NO ACTION;

--
-- Contraintes pour la table `benevoles_joint_stands`
--
ALTER TABLE `benevoles_joint_stands`
  ADD CONSTRAINT `fk_benevoles_has_stands_benevoles1` FOREIGN KEY (`benevoles_id_benevoles`) REFERENCES `benevoles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_benevoles_has_stands_stands1` FOREIGN KEY (`stands_id_stands`) REFERENCES `stands` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `dates_festivals`
--
ALTER TABLE `dates_festivals`
  ADD CONSTRAINT `fk_dates_festivals_festivals1` FOREIGN KEY (`festivals_id_festival`) REFERENCES `festivals` (`id`);

--
-- Contraintes pour la table `dates_joint_benevoles`
--
ALTER TABLE `dates_joint_benevoles`
  ADD CONSTRAINT `dates_joint_benevoles_ibfk_2` FOREIGN KEY (`dates_festivals_id`) REFERENCES `dates_festivals` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_dates_benevoles_has_benevoles_benevoles1` FOREIGN KEY (`benevole_id_benevole`) REFERENCES `benevoles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `stands`
--
ALTER TABLE `stands`
  ADD CONSTRAINT `fk_stands_festivals1` FOREIGN KEY (`festivals_id_festival`) REFERENCES `festivals` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
