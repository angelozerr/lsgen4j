package io.lsgen4j.generator;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class ProjectTemplateCopier implements FileVisitor<Path> {
	private final Path source;
	private final Path target;
	private final boolean preserve;

	ProjectTemplateCopier(Path source, Path target) {
		this.source = source;
		this.target = target;
		this.preserve = true;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		// before visiting entries in a directory we copy the directory
		// (okay if directory already exists).
		CopyOption[] options = (preserve) ? new CopyOption[] { COPY_ATTRIBUTES } : new CopyOption[0];

		Path newdir = target.resolve(source.relativize(dir));
		try {
			Files.copy(dir, newdir, options);
		} catch (FileAlreadyExistsException x) {
			// ignore
		} catch (IOException x) {
			System.err.format("Unable to create: %s: %s%n", newdir, x);
			return SKIP_SUBTREE;
		}
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		copyFile(file, target.resolve(source.relativize(file)), preserve);
		return CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		// fix up modification time of directory when done
		if (exc == null && preserve) {
			Path newdir = target.resolve(source.relativize(dir));
			try {
				FileTime time = Files.getLastModifiedTime(dir);
				Files.setLastModifiedTime(newdir, time);
			} catch (IOException x) {
				System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
			}
		}
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		if (exc instanceof FileSystemLoopException) {
			System.err.println("cycle detected: " + file);
		} else {
			System.err.format("Unable to copy: %s: %s%n", file, exc);
		}
		return CONTINUE;
	}

	/**
	 * Copy source file to target location. If {@code prompt} is true then prompt
	 * user to overwrite target if it exists. The {@code preserve} parameter
	 * determines if file attributes should be copied/preserved.
	 */
	static void copyFile(Path source, Path target, boolean preserve) {
		CopyOption[] options = (preserve) ? new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING }
				: new CopyOption[] { REPLACE_EXISTING };
		// if (!prompt || Files.notExists(target) || okayToOverwrite(target)) {
		try {
			Files.copy(source, target, options);
		} catch (IOException x) {
			System.err.format("Unable to copy: %s: %s%n", source, x);
		}
		// }
	}
}
