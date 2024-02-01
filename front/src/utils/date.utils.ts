/**
 * Formats a given date using provided formatting options.
 *
 * @param {Date | string | number} date - The date to format.
 * @param {string} [locale="en-US"] - The locale for formatting the date.
 * @param {Object} options - The formatting options for `Intl.DateTimeFormat`.
 * @returns {string} The formatted date string, or an error message if formatting fails.
 */
export function formatDateWithOptions(
  date: Date | string,
  locale: string,
  options: Intl.DateTimeFormatOptions
): string {
  try {
    const dateIsInvalid =
      // The date is invalid if the date is not an instance of the Date prototype or
      // that its type is not a string nor a number
      !(date instanceof Date) &&
      typeof date !== 'string' &&
      typeof date !== 'number';
    if (dateIsInvalid) {
      throw new TypeError(
        `Invalid date argument found, expected it to be an instance of the Date prototype or at least either a string or a number but instead got 
          \nValue: ${date}, of type: ${typeof date}`
      );
    }

    const optionsAreInvalid = !options;
    if (optionsAreInvalid) {
      throw new TypeError(
        `Invalid options argument found, expected it to be an object but instead got \nValue: ${options}, of type: ${typeof options}`
      );
    }

    const localeIsInvalid = typeof locale !== 'string';
    if (localeIsInvalid) {
      throw new TypeError(
        `Invalid locale argument found, expected it to be a string but instead got \nValue: ${locale}, of type: ${typeof locale}`
      );
    }

    const dateInstance = date instanceof Date ? date : new Date(date);

    const formatter = new Intl.DateTimeFormat(locale, options);

    return formatter.format(dateInstance);
  } catch (error: any) {
    console.error('Error formatting date:', error.message);
    return `Invalid date`;
  }
}
