/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.library.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.net.URI;
import java.util.Comparator;

@JsonDeserialize(builder = ShortcutDTO.Builder.class)
public class ShortcutDTO {
    private final String name;
    private final String category;
    private final String description;
    private final URI icon;
    private final URI miniature;
    private final String script;

    private ShortcutDTO(Builder builder) {
        name = builder.name;
        category = builder.category;
        description = builder.description;
        icon = builder.icon;
        miniature = builder.miniature;
        script = builder.script;
    }

    public URI getIcon() {
        return icon;
    }

    public URI getMiniature() {
        return miniature;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getScript() {
        return script;
    }

    public static Comparator<ShortcutDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String name;
        private String category;
        private String description;
        private URI icon;
        private URI miniature;
        private String script;

        public Builder() {
            // Default constructor
        }

        public Builder(ShortcutDTO shortcutDTO) {
            this.name = shortcutDTO.name;
            this.category = shortcutDTO.category;
            this.description = shortcutDTO.description;
            this.icon = shortcutDTO.icon;
            this.miniature = shortcutDTO.miniature;
            this.script = shortcutDTO.script;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withIcon(URI icon) {
            this.icon = icon;
            return this;
        }

        public Builder withMiniature(URI miniature) {
            this.miniature = miniature;
            return this;
        }

        public Builder withScript(String script) {
            this.script = script;
            return this;
        }

        public ShortcutDTO build() {
            return new ShortcutDTO(this);
        }

        public String getName() {
            return name;
        }
    }

}