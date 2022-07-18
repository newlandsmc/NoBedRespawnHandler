package com.semivanilla.nobedrespawnhandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;
import java.util.UUID;

public class StoredLocation {
  private double x;

  private double y;

  private double z;

  private String world;

  private UUID worldId;

  private float pitch;

  private float yaw;

  public StoredLocation(Location loc) {
    this.x = loc.getBlockX();
    this.y = loc.getBlockY();
    this.z = loc.getBlockZ();
    this.world = ((World) Objects.<World>requireNonNull(loc.getWorld())).getName();
    this.worldId = loc.getWorld().getUID();
    this.pitch = loc.getPitch();
    this.yaw = loc.getYaw();
  }

  public World getWorld() {
    return Bukkit.getWorld(this.worldId);
  }

  public StoredLocation center() {
    Location location = getLocation();
    location.add((this.x > 0.0D) ? 0.5D : -0.5D, 0.0D, 0.0D);
    return new StoredLocation(location);
  }

  public Location getLocation() {
    return new Location(Bukkit.getWorld(this.worldId), this.x, this.y, this.z, this.yaw, this.pitch);
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public double getZ() {
    return this.z;
  }

  public float getPitch() {
    return this.pitch;
  }

  public float getYaw() {
    return this.yaw;
  }

  public UUID getWorldId() {
    return this.worldId;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public void setWorldId(UUID worldId) {
    this.worldId = worldId;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public void setZ(double z) {
    this.z = z;
  }
}
