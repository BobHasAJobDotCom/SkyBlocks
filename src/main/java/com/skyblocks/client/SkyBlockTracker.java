package com.skyblocks.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class SkyBlockTracker {
	public record Entry(BlockPos pos, EnumSet<Direction> sides) {}

	private static final List<Entry> ENTRIES = new ArrayList<>();

	private SkyBlockTracker() {}

	public static void add(BlockPos pos, EnumSet<Direction> sides) {
		if (sides == null || sides.isEmpty()) {
			return;
		}
		ENTRIES.add(new Entry(pos.toImmutable(), EnumSet.copyOf(sides)));
	}

	public static List<Entry> drain() {
		List<Entry> copy = new ArrayList<>(ENTRIES);
		ENTRIES.clear();
		return copy;
	}
}
